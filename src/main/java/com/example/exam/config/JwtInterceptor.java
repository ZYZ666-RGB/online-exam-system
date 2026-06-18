package com.example.exam.config;

import com.example.exam.common.BusinessException;
import com.example.exam.common.LoginUser;
import com.example.exam.utils.JwtUtils;
import com.example.exam.utils.SecurityUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final AuthProperties authProperties;
    private final JwtUtils jwtUtils;
    private final StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String uri = request.getRequestURI();
        if (isAnonymousPath(uri)) {
            return true;
        }

        String token = resolveToken(request);
        if (token == null) {
            throw new BusinessException(401, "请先登录");
        }

        Claims claims = jwtUtils.parseToken(token);
        String jti = claims.getId();
        Boolean exists = redisTemplate.hasKey(authProperties.getRedisTokenPrefix() + jti);
        if (!Boolean.TRUE.equals(exists)) {
            throw new BusinessException(401, "登录状态已过期，请重新登录");
        }

        Long userId = Long.valueOf(claims.getSubject());
        String username = claims.get("username", String.class);
        String rolesText = claims.get("roles", String.class);
        List<String> roles = rolesText == null || rolesText.isEmpty()
                ? Collections.emptyList()
                : Arrays.stream(rolesText.split(",")).collect(Collectors.toList());
        LoginUser loginUser = new LoginUser(userId, username, roles, jti);
        SecurityUtils.setLoginUser(loginUser);

        checkRole(uri, roles);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SecurityUtils.clear();
    }

    private boolean isAnonymousPath(String uri) {
        return uri.equals("/api/auth/register") || uri.equals("/api/auth/login");
    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith(authProperties.getTokenPrefix())) {
            return null;
        }
        return header.substring(authProperties.getTokenPrefix().length());
    }

    private void checkRole(String uri, List<String> roles) {
        if ((uri.startsWith("/api/users") || uri.startsWith("/api/login-logs") || uri.startsWith("/api/roles"))
                && !roles.contains("ADMIN")) {
            throw new BusinessException(403, "无管理员权限");
        }
        if ((uri.startsWith("/api/questions") || uri.startsWith("/api/papers"))
                && !(roles.contains("TEACHER") || roles.contains("ADMIN"))) {
            throw new BusinessException(403, "无教师权限");
        }
        if ((uri.startsWith("/api/exams") || uri.startsWith("/api/wrong-questions")) && !roles.contains("STUDENT")) {
            throw new BusinessException(403, "无学生权限");
        }
        if (uri.startsWith("/api/statistics") && !(roles.contains("TEACHER") || roles.contains("ADMIN"))) {
            throw new BusinessException(403, "无统计权限");
        }
    }
}
