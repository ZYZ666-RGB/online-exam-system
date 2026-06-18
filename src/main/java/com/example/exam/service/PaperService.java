package com.example.exam.service;

import com.example.exam.dto.PaperCreateRequest;
import com.example.exam.dto.PaperQueryRequest;
import com.example.exam.dto.PaperQuestionAddRequest;
import com.example.exam.dto.PaperQuestionUpdateRequest;
import com.example.exam.vo.PageVO;
import com.example.exam.vo.PaperQuestionVO;
import com.example.exam.vo.PaperVO;

import java.util.List;

public interface PaperService {

    Long create(PaperCreateRequest request);

    PageVO<PaperVO> page(PaperQueryRequest request);

    PaperVO detail(Long id);

    void addQuestion(Long paperId, PaperQuestionAddRequest request);

    void updateQuestion(Long paperId, Long questionId, PaperQuestionUpdateRequest request);

    void removeQuestion(Long paperId, Long questionId);

    List<PaperQuestionVO> questions(Long paperId);

    void publish(Long paperId);
}
