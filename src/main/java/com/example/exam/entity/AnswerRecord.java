package com.example.exam.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnswerRecord {

    private Long id;
    private Long examId;
    private Long questionId;
    private String questionTitleSnapshot;
    private String optionSnapshot;
    private String studentAnswer;
    private String correctAnswerSnapshot;
    private Integer isCorrect;
    private Integer score;
    private LocalDateTime createTime;
}

