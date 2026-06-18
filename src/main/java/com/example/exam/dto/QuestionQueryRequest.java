package com.example.exam.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class QuestionQueryRequest {

    private String questionType;
    private String difficulty;
    private String knowledgePoint;
    private String keyword;

    @Min(1)
    private Integer pageNum = 1;

    @Min(1)
    @Max(100)
    private Integer pageSize = 10;
}

