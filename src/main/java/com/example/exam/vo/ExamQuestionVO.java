package com.example.exam.vo;

import lombok.Data;

@Data
public class ExamQuestionVO {

    private Long questionId;
    private String questionType;
    private String title;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private Integer questionScore;
    private Integer sortOrder;
}

