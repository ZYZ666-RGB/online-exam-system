package com.example.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserVO {

    private Long id;
    private String username;
    private String email;
    private String realName;
    private Integer status;
    private List<String> roles;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

