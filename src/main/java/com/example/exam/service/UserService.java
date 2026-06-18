package com.example.exam.service;

import com.example.exam.dto.LoginLogQueryRequest;
import com.example.exam.dto.UserQueryRequest;
import com.example.exam.dto.UserRolesUpdateRequest;
import com.example.exam.dto.UserStatusRequest;
import com.example.exam.vo.LoginLogVO;
import com.example.exam.vo.PageVO;
import com.example.exam.vo.RoleVO;
import com.example.exam.vo.UserVO;

import java.util.List;

public interface UserService {

    PageVO<UserVO> users(UserQueryRequest request);

    void updateStatus(Long id, UserStatusRequest request);

    List<RoleVO> roles();

    void updateRoles(Long id, UserRolesUpdateRequest request);

    PageVO<LoginLogVO> loginLogs(LoginLogQueryRequest request);
}

