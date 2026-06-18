package com.example.exam.service;

import com.example.exam.dto.ExamSubmitRequest;
import com.example.exam.vo.AvailableExamVO;
import com.example.exam.vo.ExamDetailVO;
import com.example.exam.vo.ExamQuestionVO;
import com.example.exam.vo.ExamScoreVO;

import java.util.List;

public interface ExamService {

    List<AvailableExamVO> available();

    Long start(Long paperId);

    List<ExamQuestionVO> questions(Long examId);

    Integer submit(Long examId, ExamSubmitRequest request);

    List<ExamScoreVO> myScores();

    ExamDetailVO detail(Long examId);
}

