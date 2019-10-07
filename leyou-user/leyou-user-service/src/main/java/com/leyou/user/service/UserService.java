package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LeYouException;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.pojo.UserExample;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate template;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "user:verify:code:";

    public Boolean checkData(String data, Integer type) {

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        long count;
        if (type == 1) {
            criteria.andUsernameEqualTo(data);
            count = userMapper.countByExample(userExample);
        } else if (type == 2) {
            criteria.andPhoneEqualTo(data);
            count = userMapper.countByExample(userExample);
        } else {
            throw new LeYouException(ExceptionEnum.PARAM_IS_NOT_RIGHT);
        }

        return count == 1;
    }

    /**
     * 发送验证码
     *
     * @param phone
     */
    public void sendCode(String phone) {
        // 生成验证码
        Map<String, String> param = new HashMap<>();
        String code = NumberUtils.generateCode(6);
        param.put("phoneNumber", phone);
        param.put("code", code);
        // 发送验证码
        template.convertAndSend("leyou.message.exchange", "message.verify.code", param);
        // 存储验证码(5分钟有效)
        redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
    }

    public void registry(User user, String code) {

        // 校验验证码
        // String cacheCode = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        // if (!StringUtils.equals(cacheCode, code)) {
        //     throw new LeYouException(ExceptionEnum.INVALID_VERIFY_CODE);
        // }

        // 生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);

        // 对密码加密
        String md5Hex = CodecUtils.md5Hex(user.getPassword(), salt);
        user.setPassword(md5Hex);
        user.setCreated(new Date());
        // 写入数据库
        userMapper.insertSelective(user);
    }

    public User queryUserByUsernameAndPassword(String username, String password) {


        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(username);

        List<User> userList = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(userList)) {
            throw new LeYouException(ExceptionEnum.USERNAME_OR_PASSWORD_INVALID);
        }

        if (!StringUtils.equals(password, CodecUtils.md5Hex(password, userList.get(0).getSalt()))) {
            throw new LeYouException(ExceptionEnum.USERNAME_OR_PASSWORD_INVALID);
        }

        return userList.get(0);
    }
}
