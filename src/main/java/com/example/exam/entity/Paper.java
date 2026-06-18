package com.example.exam.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Paper {

    private Long id;
    private String paperName;
    private Integer duration;
    private Integer totalScore;
    private Integer passScore;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private Long createBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

