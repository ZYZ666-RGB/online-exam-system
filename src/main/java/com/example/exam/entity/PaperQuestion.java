package com.example.exam.entity;

import lombok.Data;

@Data
public class PaperQuestion {

    private Long id;
    private Long paperId;
    private Long questionId;
    private Integer questionScore;
    private Integer sortOrder;
}

