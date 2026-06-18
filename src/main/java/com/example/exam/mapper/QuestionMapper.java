package com.example.exam.mapper;

import com.example.exam.dto.QuestionQueryRequest;
import com.example.exam.entity.Question;
import com.example.exam.vo.QuestionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionMapper {

    int insert(Question question);

    int update(Question question);

    int logicalDelete(@Param("id") Long id);

    Question selectById(@Param("id") Long id);

    QuestionVO selectDetailById(@Param("id") Long id);

    long countByQuery(@Param("query") QuestionQueryRequest query);

    List<QuestionVO> selectPage(@Param("query") QuestionQueryRequest query,
                                @Param("offset") Integer offset,
                                @Param("limit") Integer limit);
}

