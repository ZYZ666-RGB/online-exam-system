package com.example.exam.vo;

import lombok.Data;

@Data
public class PaperStatisticsVO {

    private Long paperId;
    private String paperName;
    private Integer participantCount;
    private Double averageScore;
    private Integer highestScore;
    private Integer lowestScore;
}

