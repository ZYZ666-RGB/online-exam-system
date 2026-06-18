package com.example.exam.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExamRecord {

    private Long id;
    private Long studentId;
    private Long paperId;
    private LocalDateTime startTime;
    private LocalDateTime submitTime;
    private Integer totalScore;
    private Integer status;
    private LocalDateTime createTime;
}

