package com.leyou.message.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.leyou.message.config.MessageConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created on 17/6/7.
 * 短信API产品的DEMO程序,工程中包含了一个SmsDemo类，直接通过
 * 执行main函数即可体验短信产品API功能(只需要将AK替换成开通了云通信-短信产品功能的AK即可)
 * 工程依赖了2个jar包(存放在工程的libs目录下)
 * 1:aliyun-java-sdk-core.jar
 * 2:aliyun-java-sdk-dysmsapi.jar
 * <p>
 * 备注:Demo工程编码采用UTF-8
 * 国际短信发送请勿参照此DEMO
 */

@Component
@EnableConfigurationProperties(MessageConfig.class)
@Slf4j
public class MessageUtils {

    @Autowired
    private MessageConfig messageConfig;

    @Autowired
    private StringRedisTemplate redisTemplate;

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // redis前缀
    private static final String KEY_PREFIX = "message:phone ";

    private static final Long MESSAGE_MIN_INTERVAL_IN_MILLIS = 60000L;

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    // static final String accessKeyId = "LTAIsncYQYG1Yjhj";
    // static final String accessKeySecret = "aySR4oBRliswLp87rF9gQyy1Izz4MQ";

    public SendSmsResponse sendSms(String phoneNumber, String signName, String templateCode, String templateParam) throws ClientException {

        //Redis的key
        String key = KEY_PREFIX + phoneNumber;

        String lastTime = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(lastTime)) {
            Long last = Long.valueOf(lastTime);
            if (System.currentTimeMillis() - last < MESSAGE_MIN_INTERVAL_IN_MILLIS) {
                return null;
            }

        }

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", messageConfig.getAccessKeyId(), messageConfig.getAccessKeySecret());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        //必填:待发送手机号
        request.setPhoneNumbers(phoneNumber);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(templateParam);

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        // request.setOutId("123456");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        try {
            if (!"OK".equals(sendSmsResponse.getCode())) {
                log.error("【短信服务】短信发送失败，phoneNumber={}，原因={}", phoneNumber, sendSmsResponse.getMessage());
            }

            // 发送成功，直接将当前手机的短信发送时间写入redis，指定生存时间为1分钟，到期自动删除
            redisTemplate.opsForValue().set(key, String.valueOf(System.currentTimeMillis()), 1, TimeUnit.MINUTES);

            return sendSmsResponse;
        } catch (Exception e) {
            log.error("【短信服务】短信发送异常，手机号码：{}，手机号码：{}", phoneNumber, e);
            return null;
        }
    }
}
