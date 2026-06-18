package com.example.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginLogVO {

    private Long id;
    private Long userId;
    private String username;
    private String loginIp;
    private Integer loginStatus;
    private String failReason;
    private LocalDateTime loginTime;
}

