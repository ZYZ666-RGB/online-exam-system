package com.example.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WrongQuestionVO {

    private Long id;
    private Long questionId;
    private Long examId;
    private String paperName;
    private String questionType;
    private String title;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String studentAnswer;
    private String correctAnswer;
    private String analysis;
    private LocalDateTime createTime;
}

