package com.example.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionVO {

    private Long id;
    private String questionType;
    private String title;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private String analysis;
    private Integer defaultScore;
    private String difficulty;
    private String knowledgePoint;
    private Long createBy;
    private String createByName;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

