package com.example.exam.utils;

import com.example.exam.common.BusinessException;
import com.example.exam.common.LoginUser;

public final class SecurityUtils {

    private static final ThreadLocal<LoginUser> LOGIN_USER = new ThreadLocal<>();

    private SecurityUtils() {
    }

    public static void setLoginUser(LoginUser loginUser) {
        LOGIN_USER.set(loginUser);
    }

    public static LoginUser getLoginUser() {
        LoginUser loginUser = LOGIN_USER.get();
        if (loginUser == null) {
            throw new BusinessException(401, "请先登录");
        }
        return loginUser;
    }

    public static Long getUserId() {
        return getLoginUser().getUserId();
    }

    public static void clear() {
        LOGIN_USER.remove();
    }
}

