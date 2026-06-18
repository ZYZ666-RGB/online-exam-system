package com.example.exam.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9_]{4,20}$", message = "必须为4-20位字母、数字或下划线")
    private String username;

    @NotBlank
    @Size(min = 8, max = 64, message = "长度必须为8-64位")
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(max = 50)
    private String realName;
}

