package com.leyou.user.service;

import com.leyou.user.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void checkData() {
    }

    @Test
    public void sendCode() {
    }

    @Test
    public void registry() {

        User user = new User();
        user.setPassword("you19960623+");
        user.setPhone("13540632482");
        user.setUsername("xiaochen");

        userService.registry(user, "123456");
    }
}