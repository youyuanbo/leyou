package com.leyou.message.mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MessageListenerTest {

    @Autowired
    private AmqpTemplate template;

    @Test
    public void sendMessage() {
        Map<String, String> map = new HashMap<>();
        map.put("phoneNumber", "13540632482");
        map.put("code", "521346");

        template.convertAndSend("leyou.message.exchange", "message.verify.code", map);

        try {
            Thread.sleep(100000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}