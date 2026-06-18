package com.example.exam.mapper;

import com.example.exam.entity.AnswerRecord;
import com.example.exam.entity.ExamRecord;
import com.example.exam.entity.WrongQuestion;
import com.example.exam.vo.AvailableExamVO;
import com.example.exam.vo.ExamAnswerDetailVO;
import com.example.exam.vo.ExamQuestionVO;
import com.example.exam.vo.ExamScoreVO;
import com.example.exam.vo.PaperQuestionVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ExamMapper {

    List<AvailableExamVO> selectAvailableExams(@Param("studentId") Long studentId,
                                               @Param("now") LocalDateTime now);

    ExamRecord selectRecordByStudentAndPaper(@Param("studentId") Long studentId,
                                             @Param("paperId") Long paperId);

    ExamRecord selectRecordById(@Param("examId") Long examId);

    int insertExamRecord(ExamRecord examRecord);

    List<ExamQuestionVO> selectExamQuestions(@Param("examId") Long examId,
                                             @Param("studentId") Long studentId);

    List<PaperQuestionVO> selectPaperQuestionsForJudge(@Param("paperId") Long paperId);

    int insertAnswerRecord(AnswerRecord answerRecord);

    int insertWrongQuestion(WrongQuestion wrongQuestion);

    int submitExam(@Param("examId") Long examId,
                   @Param("studentId") Long studentId,
                   @Param("totalScore") Integer totalScore,
                   @Param("submitTime") LocalDateTime submitTime);

    List<ExamScoreVO> selectMyScores(@Param("studentId") Long studentId);

    ExamScoreVO selectScoreByExamId(@Param("examId") Long examId,
                                    @Param("studentId") Long studentId);

    List<ExamAnswerDetailVO> selectAnswerDetails(@Param("examId") Long examId);
}

