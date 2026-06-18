package com.example.exam.vo;

import lombok.Data;

@Data
public class PaperQuestionVO {

    private Long id;
    private Long paperId;
    private Long questionId;
    private String questionType;
    private String title;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private String analysis;
    private Integer questionScore;
    private Integer sortOrder;
    private String difficulty;
    private String knowledgePoint;
}

