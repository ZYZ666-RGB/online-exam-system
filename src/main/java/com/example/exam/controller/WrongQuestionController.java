package com.example.exam.controller;

import com.example.exam.common.Result;
import com.example.exam.service.WrongQuestionService;
import com.example.exam.vo.WrongQuestionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wrong-questions")
@RequiredArgsConstructor
public class WrongQuestionController {

    private final WrongQuestionService wrongQuestionService;

    @GetMapping
    public Result<List<WrongQuestionVO>> listMine() {
        return Result.success(wrongQuestionService.listMine());
    }
}

