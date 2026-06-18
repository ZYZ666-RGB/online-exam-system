package com.example.exam.service.impl;

import com.example.exam.common.BusinessException;
import com.example.exam.common.LoginUser;
import com.example.exam.config.AuthProperties;
import com.example.exam.config.RegisterProperties;
import com.example.exam.dto.LoginRequest;
import com.example.exam.dto.RegisterRequest;
import com.example.exam.entity.LoginLog;
import com.example.exam.entity.SysRole;
import com.example.exam.entity.SysUser;
import com.example.exam.mapper.LoginLogMapper;
import com.example.exam.mapper.RoleMapper;
import com.example.exam.mapper.UserMapper;
import com.example.exam.service.AuthService;
import com.example.exam.utils.JwtUtils;
import com.example.exam.utils.PasswordUtils;
import com.example.exam.utils.SecurityUtils;
import com.example.exam.vo.LoginVO;
import com.example.exam.vo.UserInfoVO;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final int NORMAL_STATUS = 1;
    private static final int LOGIN_SUCCESS = 1;
    private static final int LOGIN_FAIL = 0;
    private static final int MAX_LOGIN_FAIL_COUNT = 5;
    private static final int MAX_REGISTER_COUNT_PER_MINUTE = 5;

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final LoginLogMapper loginLogMapper;
    private final PasswordUtils passwordUtils;
    private final JwtUtils jwtUtils;
    private final AuthProperties authProperties;
    private final RegisterProperties registerProperties;
    private final StringRedisTemplate redisTemplate;

    @Override
    @Transactional
    public void register(RegisterRequest request, String ip) {
        checkRegisterLimit(ip);
        checkWeakPassword(request.getUsername(), request.getPassword());
        if (userMapper.selectByUsername(request.getUsername()) != null) {
            throw new BusinessException(400, "用户名或邮箱已存在");
        }
        if (userMapper.selectByEmail(request.getEmail()) != null) {
            throw new BusinessException(400, "用户名或邮箱已存在");
        }

        SysRole defaultRole = roleMapper.selectByCode(registerProperties.getDefaultRole());
        if (defaultRole == null) {
            throw new BusinessException("默认注册角色不存在");
        }

        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordUtils.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRealName(request.getRealName());
        user.setStatus(NORMAL_STATUS);
        userMapper.insert(user);
        userMapper.insertUserRole(user.getId(), defaultRole.getId());
    }

    @Override
    public LoginVO login(LoginRequest request, String ip) {
        String failKey = authProperties.getLoginFailPrefix() + request.getUsername() + ":" + ip;
        String failCountText = redisTemplate.opsForValue().get(failKey);
        int failCount = failCountText == null ? 0 : Integer.parseInt(failCountText);
        if (failCount >= MAX_LOGIN_FAIL_COUNT) {
            throw new BusinessException(429, "登录失败次数过多，请10分钟后再试");
        }

        SysUser user = userMapper.selectByUsername(request.getUsername());
        if (user == null || !passwordUtils.matches(request.getPassword(), user.getPassword())) {
            increaseLoginFailCount(failKey);
            recordLogin(null, request.getUsername(), LOGIN_FAIL, ip, "用户名或密码错误");
            throw new BusinessException(400, "用户名或密码错误");
        }
        if (!NORMAL_STATUSEquals(user.getStatus())) {
            recordLogin(user.getId(), user.getUsername(), LOGIN_FAIL, ip, "账号已被禁用");
            throw new BusinessException(403, "账号已被禁用");
        }

        List<String> roles = userMapper.selectRoleCodesByUserId(user.getId());
        LoginUser loginUser = new LoginUser(user.getId(), user.getUsername(), roles, null);
        JwtUtils.TokenInfo tokenInfo = jwtUtils.createToken(loginUser);
        redisTemplate.opsForValue().set(
                authProperties.getRedisTokenPrefix() + tokenInfo.getJti(),
                user.getId() + "|" + user.getUsername() + "|" + String.join(",", roles),
                Duration.ofSeconds(tokenInfo.getExpireSeconds())
        );
        redisTemplate.delete(failKey);
        recordLogin(user.getId(), user.getUsername(), LOGIN_SUCCESS, ip, null);

        return new LoginVO(
                authProperties.getTokenPrefix() + tokenInfo.getToken(),
                user.getId(),
                user.getUsername(),
                user.getRealName(),
                roles,
                tokenInfo.getExpireSeconds()
        );
    }

    @Override
    public UserInfoVO userInfo() {
        Long userId = SecurityUtils.getUserId();
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(401, "用户不存在");
        }
        List<String> roles = userMapper.selectRoleCodesByUserId(userId);
        return new UserInfoVO(user.getId(), user.getUsername(), user.getRealName(), user.getEmail(), roles);
    }

    @Override
    public void logout(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(authProperties.getTokenPrefix())) {
            return;
        }
        String token = authorizationHeader.substring(authProperties.getTokenPrefix().length());
        Claims claims = jwtUtils.parseToken(token);
        redisTemplate.delete(authProperties.getRedisTokenPrefix() + claims.getId());
    }

    private void checkRegisterLimit(String ip) {
        String key = authProperties.getRegisterLimitPrefix() + ip;
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1L) {
            redisTemplate.expire(key, Duration.ofMinutes(1));
        }
        if (count != null && count > MAX_REGISTER_COUNT_PER_MINUTE) {
            throw new BusinessException(429, "注册过于频繁，请稍后再试");
        }
    }

    private void checkWeakPassword(String username, String password) {
        List<String> weakPasswords = List.of("12345678", "password", "password123", "qwerty123", "11111111");
        if (weakPasswords.contains(password.toLowerCase()) || password.equalsIgnoreCase(username)) {
            throw new BusinessException(400, "密码过于简单");
        }
    }

    private void increaseLoginFailCount(String failKey) {
        Long count = redisTemplate.opsForValue().increment(failKey);
        if (count != null && count == 1L) {
            redisTemplate.expire(failKey, Duration.ofMinutes(10));
        }
    }

    private void recordLogin(Long userId, String username, Integer status, String ip, String failReason) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(userId);
        loginLog.setUsername(username);
        loginLog.setLoginIp(ip);
        loginLog.setLoginStatus(status);
        loginLog.setFailReason(failReason);
        loginLog.setLoginTime(LocalDateTime.now());
        loginLogMapper.insert(loginLog);
    }

    private boolean NORMAL_STATUSEquals(Integer status) {
        return status != null && status == NORMAL_STATUS;
    }
}

