package com.example.exam.service.impl;

import com.example.exam.common.BusinessException;
import com.example.exam.dto.ExamSubmitRequest;
import com.example.exam.entity.AnswerRecord;
import com.example.exam.entity.ExamRecord;
import com.example.exam.entity.Paper;
import com.example.exam.entity.WrongQuestion;
import com.example.exam.mapper.ExamMapper;
import com.example.exam.mapper.PaperMapper;
import com.example.exam.service.ExamService;
import com.example.exam.utils.SecurityUtils;
import com.example.exam.vo.AvailableExamVO;
import com.example.exam.vo.ExamDetailVO;
import com.example.exam.vo.ExamQuestionVO;
import com.example.exam.vo.ExamScoreVO;
import com.example.exam.vo.PaperQuestionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private static final int PUBLISHED = 1;
    private static final int EXAM_DOING = 0;
    private static final int EXAM_SUBMITTED = 1;

    private final ExamMapper examMapper;
    private final PaperMapper paperMapper;

    @Override
    public List<AvailableExamVO> available() {
        return examMapper.selectAvailableExams(SecurityUtils.getUserId(), LocalDateTime.now());
    }

    @Override
    @Transactional
    public Long start(Long paperId) {
        Long studentId = SecurityUtils.getUserId();
        Paper paper = paperMapper.selectById(paperId);
        if (paper == null || paper.getStatus() != PUBLISHED) {
            throw new BusinessException(404, "考试不存在或未发布");
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(paper.getStartTime()) || now.isAfter(paper.getEndTime())) {
            throw new BusinessException(400, "当前不在考试时间范围内");
        }

        ExamRecord old = examMapper.selectRecordByStudentAndPaper(studentId, paperId);
        if (old != null) {
            return old.getId();
        }

        ExamRecord examRecord = new ExamRecord();
        examRecord.setStudentId(studentId);
        examRecord.setPaperId(paperId);
        examRecord.setStartTime(now);
        examRecord.setTotalScore(0);
        examRecord.setStatus(EXAM_DOING);
        examMapper.insertExamRecord(examRecord);
        return examRecord.getId();
    }

    @Override
    public List<ExamQuestionVO> questions(Long examId) {
        Long studentId = SecurityUtils.getUserId();
        ExamRecord record = requireOwnExam(examId, studentId);
        if (record.getStatus() == EXAM_SUBMITTED) {
            throw new BusinessException(400, "试卷已提交");
        }
        return examMapper.selectExamQuestions(examId, studentId);
    }

    @Override
    @Transactional
    public Integer submit(Long examId, ExamSubmitRequest request) {
        Long studentId = SecurityUtils.getUserId();
        ExamRecord record = requireOwnExam(examId, studentId);
        if (record.getStatus() == EXAM_SUBMITTED) {
            throw new BusinessException(400, "试卷已提交，不能重复提交");
        }

        Paper paper = paperMapper.selectById(record.getPaperId());
        LocalDateTime now = LocalDateTime.now();
        if (paper == null || now.isAfter(paper.getEndTime())) {
            throw new BusinessException(400, "考试已结束，无法提交");
        }

        Map<Long, String> submittedAnswers = new HashMap<>();
        for (ExamSubmitRequest.AnswerSubmitItem item : request.getAnswers()) {
            submittedAnswers.put(item.getQuestionId(), normalizeAnswer(item.getStudentAnswer()));
        }

        List<PaperQuestionVO> paperQuestions = examMapper.selectPaperQuestionsForJudge(record.getPaperId());
        if (paperQuestions.isEmpty()) {
            throw new BusinessException(400, "试卷没有题目");
        }

        int totalScore = 0;
        for (PaperQuestionVO question : paperQuestions) {
            String studentAnswer = submittedAnswers.getOrDefault(question.getQuestionId(), "");
            String correctAnswer = normalizeAnswer(question.getCorrectAnswer());
            boolean correct = correctAnswer.equals(studentAnswer);
            int score = correct ? question.getQuestionScore() : 0;
            totalScore += score;

            AnswerRecord answerRecord = new AnswerRecord();
            answerRecord.setExamId(examId);
            answerRecord.setQuestionId(question.getQuestionId());
            answerRecord.setQuestionTitleSnapshot(question.getTitle());
            answerRecord.setOptionSnapshot(buildOptionSnapshot(question));
            answerRecord.setStudentAnswer(studentAnswer);
            answerRecord.setCorrectAnswerSnapshot(correctAnswer);
            answerRecord.setIsCorrect(correct ? 1 : 0);
            answerRecord.setScore(score);
            answerRecord.setCreateTime(now);
            examMapper.insertAnswerRecord(answerRecord);

            if (!correct) {
                WrongQuestion wrongQuestion = new WrongQuestion();
                wrongQuestion.setStudentId(studentId);
                wrongQuestion.setQuestionId(question.getQuestionId());
                wrongQuestion.setExamId(examId);
                wrongQuestion.setStudentAnswer(studentAnswer);
                wrongQuestion.setCorrectAnswer(correctAnswer);
                wrongQuestion.setCreateTime(now);
                examMapper.insertWrongQuestion(wrongQuestion);
            }
        }

        int updated = examMapper.submitExam(examId, studentId, totalScore, now);
        if (updated == 0) {
            throw new BusinessException(400, "试卷提交失败，请勿重复提交");
        }
        return totalScore;
    }

    @Override
    public List<ExamScoreVO> myScores() {
        return examMapper.selectMyScores(SecurityUtils.getUserId());
    }

    @Override
    public ExamDetailVO detail(Long examId) {
        Long studentId = SecurityUtils.getUserId();
        ExamScoreVO score = examMapper.selectScoreByExamId(examId, studentId);
        if (score == null) {
            throw new BusinessException(404, "考试记录不存在");
        }
        ExamDetailVO detail = new ExamDetailVO();
        detail.setScore(score);
        detail.setAnswers(examMapper.selectAnswerDetails(examId));
        return detail;
    }

    private ExamRecord requireOwnExam(Long examId, Long studentId) {
        ExamRecord record = examMapper.selectRecordById(examId);
        if (record == null || !record.getStudentId().equals(studentId)) {
            throw new BusinessException(404, "考试记录不存在");
        }
        return record;
    }

    private String normalizeAnswer(String answer) {
        if (answer == null || answer.isBlank()) {
            return "";
        }
        return java.util.Arrays.stream(answer.toUpperCase().replace(" ", "").split(","))
                .filter(item -> !item.isBlank())
                .distinct()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.joining(","));
    }

    private String buildOptionSnapshot(PaperQuestionVO question) {
        return "{\"A\":\"" + escapeJson(question.getOptionA()) + "\","
                + "\"B\":\"" + escapeJson(question.getOptionB()) + "\","
                + "\"C\":\"" + escapeJson(question.getOptionC()) + "\","
                + "\"D\":\"" + escapeJson(question.getOptionD()) + "\"}";
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
