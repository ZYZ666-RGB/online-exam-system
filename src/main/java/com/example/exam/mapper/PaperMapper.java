package com.example.exam.mapper;

import com.example.exam.dto.PaperQueryRequest;
import com.example.exam.entity.Paper;
import com.example.exam.entity.PaperQuestion;
import com.example.exam.vo.PaperQuestionVO;
import com.example.exam.vo.PaperVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaperMapper {

    int insert(Paper paper);

    Paper selectById(@Param("id") Long id);

    PaperVO selectDetailById(@Param("id") Long id);

    long countByQuery(@Param("query") PaperQueryRequest query);

    List<PaperVO> selectPage(@Param("query") PaperQueryRequest query,
                             @Param("offset") Integer offset,
                             @Param("limit") Integer limit);

    int insertPaperQuestion(PaperQuestion paperQuestion);

    int updatePaperQuestionScore(@Param("paperId") Long paperId,
                                 @Param("questionId") Long questionId,
                                 @Param("questionScore") Integer questionScore);

    int deletePaperQuestion(@Param("paperId") Long paperId, @Param("questionId") Long questionId);

    List<PaperQuestionVO> selectQuestionsByPaperId(@Param("paperId") Long paperId);

    int countQuestions(@Param("paperId") Long paperId);

    Integer sumQuestionScore(@Param("paperId") Long paperId);

    int publish(@Param("paperId") Long paperId,
                @Param("totalScore") Integer totalScore,
                @Param("passScore") Integer passScore);
}
