package com.example.exam.service.impl;

import com.example.exam.common.BusinessException;
import com.example.exam.entity.Paper;
import com.example.exam.mapper.PaperMapper;
import com.example.exam.mapper.StatisticsMapper;
import com.example.exam.service.StatisticsService;
import com.example.exam.vo.PaperExamRecordVO;
import com.example.exam.vo.PaperStatisticsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsMapper statisticsMapper;
    private final PaperMapper paperMapper;

    @Override
    public PaperStatisticsVO paperStatistics(Long paperId) {
        requirePaper(paperId);
        return statisticsMapper.selectPaperStatistics(paperId);
    }

    @Override
    public List<PaperExamRecordVO> paperRecords(Long paperId) {
        requirePaper(paperId);
        return statisticsMapper.selectPaperRecords(paperId);
    }

    private void requirePaper(Long paperId) {
        Paper paper = paperMapper.selectById(paperId);
        if (paper == null) {
            throw new BusinessException(404, "试卷不存在");
        }
    }
}

