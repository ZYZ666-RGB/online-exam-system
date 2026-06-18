package com.example.exam.controller;

import com.example.exam.common.Result;
import com.example.exam.dto.PaperCreateRequest;
import com.example.exam.dto.PaperQueryRequest;
import com.example.exam.dto.PaperQuestionAddRequest;
import com.example.exam.dto.PaperQuestionUpdateRequest;
import com.example.exam.service.PaperService;
import com.example.exam.vo.PageVO;
import com.example.exam.vo.PaperQuestionVO;
import com.example.exam.vo.PaperVO;
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
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/papers")
@RequiredArgsConstructor
public class PaperController {

    private final PaperService paperService;

    @PostMapping
    public Result<Long> create(@Valid @RequestBody PaperCreateRequest request) {
        return Result.success(paperService.create(request));
    }

    @GetMapping
    public Result<PageVO<PaperVO>> page(@Valid PaperQueryRequest request) {
        return Result.success(paperService.page(request));
    }

    @GetMapping("/{id}")
    public Result<PaperVO> detail(@PathVariable Long id) {
        return Result.success(paperService.detail(id));
    }

    @PostMapping("/{paperId}/questions")
    public Result<Void> addQuestion(@PathVariable Long paperId,
                                    @Valid @RequestBody PaperQuestionAddRequest request) {
        paperService.addQuestion(paperId, request);
        return Result.success();
    }

    @DeleteMapping("/{paperId}/questions/{questionId}")
    public Result<Void> removeQuestion(@PathVariable Long paperId, @PathVariable Long questionId) {
        paperService.removeQuestion(paperId, questionId);
        return Result.success();
    }

    @PutMapping("/{paperId}/questions/{questionId}")
    public Result<Void> updateQuestion(@PathVariable Long paperId,
                                       @PathVariable Long questionId,
                                       @Valid @RequestBody PaperQuestionUpdateRequest request) {
        paperService.updateQuestion(paperId, questionId, request);
        return Result.success();
    }

    @GetMapping("/{paperId}/questions")
    public Result<List<PaperQuestionVO>> questions(@PathVariable Long paperId) {
        return Result.success(paperService.questions(paperId));
    }

    @PutMapping("/{paperId}/publish")
    public Result<Void> publish(@PathVariable Long paperId) {
        paperService.publish(paperId);
        return Result.success();
    }
}
