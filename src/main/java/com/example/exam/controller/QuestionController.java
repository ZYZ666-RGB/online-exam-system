package com.example.exam.controller;

import com.example.exam.common.Result;
import com.example.exam.dto.QuestionCreateRequest;
import com.example.exam.dto.QuestionQueryRequest;
import com.example.exam.dto.QuestionUpdateRequest;
import com.example.exam.service.QuestionService;
import com.example.exam.vo.PageVO;
import com.example.exam.vo.QuestionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public Result<Long> create(@Valid @RequestBody QuestionCreateRequest request) {
        return Result.success(questionService.create(request));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody QuestionUpdateRequest request) {
        questionService.update(id, request);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        questionService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<QuestionVO> detail(@PathVariable Long id) {
        return Result.success(questionService.detail(id));
    }

    @GetMapping
    public Result<PageVO<QuestionVO>> page(@Valid QuestionQueryRequest request) {
        return Result.success(questionService.page(request));
    }
}

