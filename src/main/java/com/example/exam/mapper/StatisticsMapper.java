package com.example.exam.mapper;

import com.example.exam.vo.PaperExamRecordVO;
import com.example.exam.vo.PaperStatisticsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StatisticsMapper {

    PaperStatisticsVO selectPaperStatistics(@Param("paperId") Long paperId);

    List<PaperExamRecordVO> selectPaperRecords(@Param("paperId") Long paperId);
}

