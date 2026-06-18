package com.example.exam.vo;

import lombok.Data;

import java.util.List;

@Data
public class ExamDetailVO {

    private ExamScoreVO score;
    private List<ExamAnswerDetailVO> answers;
}

