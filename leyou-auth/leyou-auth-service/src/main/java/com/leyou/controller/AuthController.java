package com.leyou.controller;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LeYouException;
import com.leyou.common.utils.CookieUtils;
import com.leyou.config.JWTProperties;
import com.leyou.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Value(value = "leyou.jwt.cookieName")
    private String cookieName;

    @Autowired
    private JWTProperties jwtProperties;

    @PostMapping("login")
    public ResponseEntity<String> login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        // 生成token
        String token = authService.login(username, password);
        // 将token写入Cookie
        // response.addCookie(new Cookie("leyou-token", token));
        CookieUtils.setCookie(request, response, cookieName, token);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(
            @CookieValue(name = "LEYOU_TOKEN") String token,
            HttpServletRequest request,
            HttpServletResponse response) {
        // 如果token为空，直接返回
        if (StringUtils.isBlank(token)) {
            throw new LeYouException(ExceptionEnum.UNAUTHORIZED);
        }

        try {
            // 从token中解析用户信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            // 解析成功后，需要刷新token
            token = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            // 更新Cookie中的token
            CookieUtils.setCookie(request, response, cookieName, token);
            // 返回用户信息
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            // 未授权或者token已过期
            throw new LeYouException(ExceptionEnum.UNAUTHORIZED);
        }

        // 出现异常相应， 返回500
        // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
