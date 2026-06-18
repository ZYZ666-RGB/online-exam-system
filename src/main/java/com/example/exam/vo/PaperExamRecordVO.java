package com.example.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaperExamRecordVO {

    private Long examId;
    private Long studentId;
    private String studentName;
    private String username;
    private Integer totalScore;
    private Integer passScore;
    private Integer isPass;
    private LocalDateTime startTime;
    private LocalDateTime submitTime;
}

