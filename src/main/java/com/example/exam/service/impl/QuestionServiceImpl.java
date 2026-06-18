package com.example.exam.service.impl;

import com.example.exam.common.BusinessException;
import com.example.exam.dto.QuestionCreateRequest;
import com.example.exam.dto.QuestionQueryRequest;
import com.example.exam.dto.QuestionUpdateRequest;
import com.example.exam.entity.Question;
import com.example.exam.mapper.QuestionMapper;
import com.example.exam.service.QuestionService;
import com.example.exam.utils.SecurityUtils;
import com.example.exam.vo.PageVO;
import com.example.exam.vo.QuestionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private static final Set<String> QUESTION_TYPES = Set.of("single", "multiple", "judge");
    private static final Set<String> DIFFICULTIES = Set.of("easy", "medium", "hard");

    private final QuestionMapper questionMapper;

    @Override
    @Transactional
    public Long create(QuestionCreateRequest request) {
        validateQuestion(request.getQuestionType(), request.getDifficulty(), request.getCorrectAnswer());
        Question question = new Question();
        fillQuestion(question, request);
        question.setCreateBy(SecurityUtils.getUserId());
        question.setStatus(1);
        questionMapper.insert(question);
        return question.getId();
    }

    @Override
    @Transactional
    public void update(Long id, QuestionUpdateRequest request) {
        validateQuestion(request.getQuestionType(), request.getDifficulty(), request.getCorrectAnswer());
        Question old = questionMapper.selectById(id);
        if (old == null || old.getStatus() == 0) {
            throw new BusinessException(404, "题目不存在");
        }
        Question question = new Question();
        question.setId(id);
        fillQuestion(question, request);
        questionMapper.update(question);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Question old = questionMapper.selectById(id);
        if (old == null || old.getStatus() == 0) {
            throw new BusinessException(404, "题目不存在");
        }
        questionMapper.logicalDelete(id);
    }

    @Override
    public QuestionVO detail(Long id) {
        QuestionVO detail = questionMapper.selectDetailById(id);
        if (detail == null || detail.getStatus() == 0) {
            throw new BusinessException(404, "题目不存在");
        }
        return detail;
    }

    @Override
    public PageVO<QuestionVO> page(QuestionQueryRequest request) {
        int pageNum = request.getPageNum() == null ? 1 : request.getPageNum();
        int pageSize = request.getPageSize() == null ? 10 : request.getPageSize();
        int offset = (pageNum - 1) * pageSize;
        long total = questionMapper.countByQuery(request);
        List<QuestionVO> records = total == 0
                ? List.of()
                : questionMapper.selectPage(request, offset, pageSize);
        return new PageVO<>(total, pageNum, pageSize, records);
    }

    private void fillQuestion(Question question, QuestionCreateRequest request) {
        question.setQuestionType(request.getQuestionType());
        question.setTitle(request.getTitle());
        question.setOptionA(request.getOptionA());
        question.setOptionB(request.getOptionB());
        question.setOptionC(request.getOptionC());
        question.setOptionD(request.getOptionD());
        question.setCorrectAnswer(normalizeAnswer(request.getCorrectAnswer()));
        question.setAnalysis(request.getAnalysis());
        question.setDefaultScore(request.getDefaultScore());
        question.setDifficulty(request.getDifficulty());
        question.setKnowledgePoint(request.getKnowledgePoint());
    }

    private void fillQuestion(Question question, QuestionUpdateRequest request) {
        question.setQuestionType(request.getQuestionType());
        question.setTitle(request.getTitle());
        question.setOptionA(request.getOptionA());
        question.setOptionB(request.getOptionB());
        question.setOptionC(request.getOptionC());
        question.setOptionD(request.getOptionD());
        question.setCorrectAnswer(normalizeAnswer(request.getCorrectAnswer()));
        question.setAnalysis(request.getAnalysis());
        question.setDefaultScore(request.getDefaultScore());
        question.setDifficulty(request.getDifficulty());
        question.setKnowledgePoint(request.getKnowledgePoint());
    }

    private void validateQuestion(String questionType, String difficulty, String correctAnswer) {
        if (!QUESTION_TYPES.contains(questionType)) {
            throw new BusinessException(400, "题型必须为single、multiple或judge");
        }
        if (!DIFFICULTIES.contains(difficulty)) {
            throw new BusinessException(400, "难度必须为easy、medium或hard");
        }
        String answer = normalizeAnswer(correctAnswer);
        if ("judge".equals(questionType) && !("A".equals(answer) || "B".equals(answer))) {
            throw new BusinessException(400, "判断题答案必须为A或B");
        }
        if ("single".equals(questionType) && !answer.matches("[A-D]")) {
            throw new BusinessException(400, "单选题答案必须为A、B、C或D");
        }
        if ("multiple".equals(questionType) && !answer.matches("[A-D](,[A-D]){1,3}")) {
            throw new BusinessException(400, "多选题答案格式必须类似A,B,C");
        }
    }

    private String normalizeAnswer(String answer) {
        String[] parts = answer.toUpperCase().replace(" ", "").split(",");
        return String.join(",", java.util.Arrays.stream(parts).distinct().sorted().toArray(String[]::new));
    }
}

