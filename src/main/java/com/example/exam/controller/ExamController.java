package com.example.exam.controller;

import com.example.exam.common.Result;
import com.example.exam.dto.ExamSubmitRequest;
import com.example.exam.service.ExamService;
import com.example.exam.vo.AvailableExamVO;
import com.example.exam.vo.ExamDetailVO;
import com.example.exam.vo.ExamQuestionVO;
import com.example.exam.vo.ExamScoreVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @GetMapping("/available")
    public Result<List<AvailableExamVO>> available() {
        return Result.success(examService.available());
    }

    @PostMapping("/{paperId}/start")
    public Result<Long> start(@PathVariable Long paperId) {
        return Result.success(examService.start(paperId));
    }

    @GetMapping("/{examId}/questions")
    public Result<List<ExamQuestionVO>> questions(@PathVariable Long examId) {
        return Result.success(examService.questions(examId));
    }

    @PostMapping("/{examId}/submit")
    public Result<Integer> submit(@PathVariable Long examId, @Valid @RequestBody ExamSubmitRequest request) {
        return Result.success(examService.submit(examId, request));
    }

    @GetMapping("/my-scores")
    public Result<List<ExamScoreVO>> myScores() {
        return Result.success(examService.myScores());
    }

    @GetMapping("/{examId}/detail")
    public Result<ExamDetailVO> detail(@PathVariable Long examId) {
        return Result.success(examService.detail(examId));
    }
}

