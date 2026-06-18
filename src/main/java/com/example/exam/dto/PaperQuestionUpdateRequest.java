package com.example.exam.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PaperQuestionUpdateRequest {

    @NotNull
    @Min(1)
    @Max(100)
    private Integer questionScore;
}
