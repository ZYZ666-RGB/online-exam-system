package com.example.exam.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class PaperCreateRequest {

    @NotBlank
    @Size(max = 100)
    private String paperName;

    @NotNull
    @Min(1)
    private Integer duration;

    @NotNull
    @Min(1)
    private Integer passScore;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;
}

