package com.example.exam.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO {

    private Long userId;
    private String username;
    private String realName;
    private String email;
    private List<String> roles;
}

