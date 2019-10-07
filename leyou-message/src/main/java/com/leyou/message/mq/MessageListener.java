package com.leyou.message.mq;

import com.aliyuncs.exceptions.ClientException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.message.config.MessageConfig;
import com.leyou.message.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Component
@EnableConfigurationProperties(MessageConfig.class)
@Slf4j
public class MessageListener {

    @Autowired
    private MessageUtils messageUtils;

    @Autowired
    private MessageConfig messageConfig;

    /**
     * 发送短信验证码
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "message.verify.code.queue", durable = "true"),
            exchange = @Exchange(name = "leyou.message.exchange", type = ExchangeTypes.TOPIC),
            key = {"message.verify.code"}
    ))
    public void sendMessage(Map<String, String> message) {
        if (CollectionUtils.isEmpty(message)) {
            return;
        }

        String phoneNumber = message.remove("phoneNumber");
        if (StringUtils.isBlank(phoneNumber)) {
            return;
        }

        String templateParam = JsonUtils.serialize(message);

        String signName = messageConfig.getSignName();

        String verifyCodeTemplate = messageConfig.getVerifyCodeTemplate();

        try {
            messageUtils.sendSms(phoneNumber, signName, verifyCodeTemplate, templateParam);
        } catch (ClientException e) {
            log.error("【短信服务】短信发送异常，手机号码：{}，异常详情：{}", phoneNumber, e);
        }
    }

}
