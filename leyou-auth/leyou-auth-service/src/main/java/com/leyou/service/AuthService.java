package com.leyou.service;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.client.UserClient;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LeYouException;
import com.leyou.config.JWTProperties;
import com.leyou.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JWTProperties jwtProperties;

    public String login(String username, String password) {

        // 校验用户名与密码
        User user = userClient.queryUserByUsernameAndPassword(username, password);
        if (user == null) {
            throw new LeYouException(ExceptionEnum.USERNAME_OR_PASSWORD_INVALID);
        }

        // 生成token
        try {
            return JwtUtils.generateToken(new UserInfo(user.getId(), username), jwtProperties.getPrivateKey(), jwtProperties.getExpire());
        } catch (Exception e) {
            log.error("【授权中心】 生成token失败，用户名：{}", username, e);
            throw new LeYouException(ExceptionEnum.CREATE_TOKEN_ERROR);
        }
    }
}
