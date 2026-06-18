package com.example.exam.service;

import com.example.exam.vo.PaperExamRecordVO;
import com.example.exam.vo.PaperStatisticsVO;

import java.util.List;

public interface StatisticsService {

    PaperStatisticsVO paperStatistics(Long paperId);

    List<PaperExamRecordVO> paperRecords(Long paperId);
}

