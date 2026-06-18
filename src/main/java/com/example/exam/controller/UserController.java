package com.example.exam.controller;

import com.example.exam.common.Result;
import com.example.exam.dto.LoginLogQueryRequest;
import com.example.exam.dto.UserQueryRequest;
import com.example.exam.dto.UserRolesUpdateRequest;
import com.example.exam.dto.UserStatusRequest;
import com.example.exam.service.UserService;
import com.example.exam.vo.LoginLogVO;
import com.example.exam.vo.PageVO;
import com.example.exam.vo.RoleVO;
import com.example.exam.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/api/users")
    public Result<PageVO<UserVO>> users(@Valid UserQueryRequest request) {
        return Result.success(userService.users(request));
    }

    @PutMapping("/api/users/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @Valid @RequestBody UserStatusRequest request) {
        userService.updateStatus(id, request);
        return Result.success();
    }

    @GetMapping("/api/roles")
    public Result<List<RoleVO>> roles() {
        return Result.success(userService.roles());
    }

    @PutMapping("/api/users/{id}/roles")
    public Result<Void> updateRoles(@PathVariable Long id, @Valid @RequestBody UserRolesUpdateRequest request) {
        userService.updateRoles(id, request);
        return Result.success();
    }

    @GetMapping("/api/login-logs")
    public Result<PageVO<LoginLogVO>> loginLogs(@Valid LoginLogQueryRequest request) {
        return Result.success(userService.loginLogs(request));
    }
}

