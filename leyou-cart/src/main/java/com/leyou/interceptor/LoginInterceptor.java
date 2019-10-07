package com.leyou.interceptor;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.cart.config.JWTProperties;
import com.leyou.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@EnableConfigurationProperties(value = JWTProperties.class)
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JWTProperties jwtProperties;

    private static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 查询token
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        if (StringUtils.isBlank(token)) {
            // 未登录，返回401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        // 有token，查询用户信息
        try {
            // 解析成功，说明已经登录
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            // 将用户信息存入线程域内
            THREAD_LOCAL.set(userInfo);
            return true;
        } catch (Exception e) {
            // 如果解析失败，说明未登录，返回404
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        THREAD_LOCAL.remove();
    }

    public static UserInfo getLoginUser() {
        return THREAD_LOCAL.get();
    }
}
