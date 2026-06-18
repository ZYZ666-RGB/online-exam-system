package com.example.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PaperVO {

    private Long id;
    private String paperName;
    private Integer duration;
    private Integer totalScore;
    private Integer passScore;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private Long createBy;
    private String createByName;
    private Integer questionCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<PaperQuestionVO> questions;
}

