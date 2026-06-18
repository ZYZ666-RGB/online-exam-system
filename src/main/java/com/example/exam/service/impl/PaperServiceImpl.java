package com.example.exam.service.impl;

import com.example.exam.common.BusinessException;
import com.example.exam.dto.PaperCreateRequest;
import com.example.exam.dto.PaperQueryRequest;
import com.example.exam.dto.PaperQuestionAddRequest;
import com.example.exam.dto.PaperQuestionUpdateRequest;
import com.example.exam.entity.Paper;
import com.example.exam.entity.PaperQuestion;
import com.example.exam.entity.Question;
import com.example.exam.mapper.PaperMapper;
import com.example.exam.mapper.QuestionMapper;
import com.example.exam.service.PaperService;
import com.example.exam.utils.SecurityUtils;
import com.example.exam.vo.PageVO;
import com.example.exam.vo.PaperQuestionVO;
import com.example.exam.vo.PaperVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaperServiceImpl implements PaperService {

    private static final int DRAFT = 0;
    private static final int PUBLISHED = 1;

    private final PaperMapper paperMapper;
    private final QuestionMapper questionMapper;

    @Override
    @Transactional
    public Long create(PaperCreateRequest request) {
        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new BusinessException(400, "开始时间必须早于结束时间");
        }
        Paper paper = new Paper();
        paper.setPaperName(request.getPaperName());
        paper.setDuration(request.getDuration());
        paper.setTotalScore(0);
        paper.setPassScore(request.getPassScore());
        paper.setStartTime(request.getStartTime());
        paper.setEndTime(request.getEndTime());
        paper.setStatus(DRAFT);
        paper.setCreateBy(SecurityUtils.getUserId());
        paperMapper.insert(paper);
        return paper.getId();
    }

    @Override
    public PageVO<PaperVO> page(PaperQueryRequest request) {
        int pageNum = request.getPageNum() == null ? 1 : request.getPageNum();
        int pageSize = request.getPageSize() == null ? 10 : request.getPageSize();
        int offset = (pageNum - 1) * pageSize;
        long total = paperMapper.countByQuery(request);
        List<PaperVO> records = total == 0 ? List.of() : paperMapper.selectPage(request, offset, pageSize);
        return new PageVO<>(total, pageNum, pageSize, records);
    }

    @Override
    public PaperVO detail(Long id) {
        PaperVO paper = paperMapper.selectDetailById(id);
        if (paper == null) {
            throw new BusinessException(404, "试卷不存在");
        }
        paper.setQuestions(paperMapper.selectQuestionsByPaperId(id));
        return paper;
    }

    @Override
    @Transactional
    public void addQuestion(Long paperId, PaperQuestionAddRequest request) {
        Paper paper = requireDraftPaper(paperId);
        Question question = questionMapper.selectById(request.getQuestionId());
        if (question == null || question.getStatus() == 0) {
            throw new BusinessException(404, "题目不存在");
        }
        PaperQuestion paperQuestion = new PaperQuestion();
        paperQuestion.setPaperId(paper.getId());
        paperQuestion.setQuestionId(request.getQuestionId());
        paperQuestion.setQuestionScore(request.getQuestionScore());
        paperQuestion.setSortOrder(request.getSortOrder());
        paperMapper.insertPaperQuestion(paperQuestion);
    }

    @Override
    @Transactional
    public void updateQuestion(Long paperId, Long questionId, PaperQuestionUpdateRequest request) {
        requireDraftPaper(paperId);
        int rows = paperMapper.updatePaperQuestionScore(paperId, questionId, request.getQuestionScore());
        if (rows == 0) {
            throw new BusinessException(404, "试卷中不存在该题目");
        }
    }

    @Override
    @Transactional
    public void removeQuestion(Long paperId, Long questionId) {
        requireDraftPaper(paperId);
        int rows = paperMapper.deletePaperQuestion(paperId, questionId);
        if (rows == 0) {
            throw new BusinessException(404, "试卷中不存在该题目");
        }
    }

    @Override
    public List<PaperQuestionVO> questions(Long paperId) {
        Paper paper = paperMapper.selectById(paperId);
        if (paper == null) {
            throw new BusinessException(404, "试卷不存在");
        }
        return paperMapper.selectQuestionsByPaperId(paperId);
    }

    @Override
    @Transactional
    public void publish(Long paperId) {
        Paper paper = requireDraftPaper(paperId);
        int questionCount = paperMapper.countQuestions(paperId);
        if (questionCount == 0) {
            throw new BusinessException(400, "试卷至少需要包含一道题目");
        }
        if (paper.getEndTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(400, "试卷结束时间已过，不能发布");
        }
        Integer totalScore = paperMapper.sumQuestionScore(paperId);
        if (totalScore == null || totalScore <= 0) {
            throw new BusinessException(400, "试卷总分必须大于0");
        }
        if (paper.getPassScore() > totalScore) {
            throw new BusinessException(400, "及格分不能大于总分");
        }
        paperMapper.publish(paperId, totalScore, paper.getPassScore());
    }

    private Paper requireDraftPaper(Long paperId) {
        Paper paper = paperMapper.selectById(paperId);
        if (paper == null) {
            throw new BusinessException(404, "试卷不存在");
        }
        if (paper.getStatus() != DRAFT) {
            throw new BusinessException(400, "只有草稿试卷可以修改");
        }
        return paper;
    }
}
