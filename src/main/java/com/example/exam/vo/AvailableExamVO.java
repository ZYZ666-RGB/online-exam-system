package com.example.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AvailableExamVO {

    private Long paperId;
    private String paperName;
    private Integer duration;
    private Integer totalScore;
    private Integer passScore;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long examId;
    private Integer examStatus;
}

