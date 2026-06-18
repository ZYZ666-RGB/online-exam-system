package com.example.exam.mapper;

import com.example.exam.vo.WrongQuestionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WrongQuestionMapper {

    List<WrongQuestionVO> selectByStudentId(@Param("studentId") Long studentId);
}

