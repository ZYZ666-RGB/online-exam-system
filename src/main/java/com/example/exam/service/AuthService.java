package com.example.exam.service;

import com.example.exam.dto.LoginRequest;
import com.example.exam.dto.RegisterRequest;
import com.example.exam.vo.LoginVO;
import com.example.exam.vo.UserInfoVO;

public interface AuthService {

    void register(RegisterRequest request, String ip);

    LoginVO login(LoginRequest request, String ip);

    UserInfoVO userInfo();

    void logout(String authorizationHeader);
}

