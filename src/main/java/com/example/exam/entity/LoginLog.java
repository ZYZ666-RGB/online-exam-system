package com.example.exam.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginLog {

    private Long id;
    private Long userId;
    private String username;
    private String loginIp;
    private Integer loginStatus;
    private String failReason;
    private LocalDateTime loginTime;
}

