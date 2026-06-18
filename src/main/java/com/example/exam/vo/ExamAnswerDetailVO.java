package com.example.exam.vo;

import lombok.Data;

@Data
public class ExamAnswerDetailVO {

    private Long questionId;
    private String questionTitleSnapshot;
    private String optionSnapshot;
    private String studentAnswer;
    private String correctAnswerSnapshot;
    private Integer isCorrect;
    private Integer score;
}

