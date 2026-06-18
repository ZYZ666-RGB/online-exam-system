package com.example.exam.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserStatusRequest {

    @NotNull
    private Integer status;
}

