package com.example.exam.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PaperQuestionAddRequest {

    @NotNull
    private Long questionId;

    @NotNull
    @Min(1)
    private Integer questionScore;

    @NotNull
    @Min(1)
    private Integer sortOrder;
}

