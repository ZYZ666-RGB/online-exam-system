package com.example.exam.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ExamSubmitRequest {

    @Valid
    @NotEmpty
    private List<AnswerSubmitItem> answers;

    @Data
    public static class AnswerSubmitItem {

        @NotNull
        private Long questionId;

        private String studentAnswer;
    }
}

