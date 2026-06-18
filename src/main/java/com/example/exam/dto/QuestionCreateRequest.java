package com.example.exam.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class QuestionCreateRequest {

    @NotBlank
    private String questionType;

    @NotBlank
    private String title;

    @NotBlank
    @Size(max = 500)
    private String optionA;

    @NotBlank
    @Size(max = 500)
    private String optionB;

    @Size(max = 500)
    private String optionC;

    @Size(max = 500)
    private String optionD;

    @NotBlank
    @Size(max = 255)
    private String correctAnswer;

    private String analysis;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer defaultScore;

    @NotBlank
    private String difficulty;

    @NotBlank
    @Size(max = 100)
    private String knowledgePoint;
}

