package com.example.exam.controller;

import com.example.exam.common.Result;
import com.example.exam.service.StatisticsService;
import com.example.exam.vo.PaperExamRecordVO;
import com.example.exam.vo.PaperStatisticsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/paper/{paperId}")
    public Result<PaperStatisticsVO> paperStatistics(@PathVariable Long paperId) {
        return Result.success(statisticsService.paperStatistics(paperId));
    }

    @GetMapping("/paper/{paperId}/records")
    public Result<List<PaperExamRecordVO>> paperRecords(@PathVariable Long paperId) {
        return Result.success(statisticsService.paperRecords(paperId));
    }
}

