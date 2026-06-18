package com.example.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExamScoreVO {

    private Long examId;
    private Long studentId;
    private String studentName;
    private Long paperId;
    private String paperName;
    private Integer totalScore;
    private Integer passScore;
    private LocalDateTime submitTime;
    private Integer isPass;
}

