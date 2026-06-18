package com.example.exam.service;

import com.example.exam.dto.QuestionCreateRequest;
import com.example.exam.dto.QuestionQueryRequest;
import com.example.exam.dto.QuestionUpdateRequest;
import com.example.exam.vo.PageVO;
import com.example.exam.vo.QuestionVO;

public interface QuestionService {

    Long create(QuestionCreateRequest request);

    void update(Long id, QuestionUpdateRequest request);

    void delete(Long id);

    QuestionVO detail(Long id);

    PageVO<QuestionVO> page(QuestionQueryRequest request);
}

