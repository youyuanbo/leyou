package com.leyou.config;

import com.leyou.auth.utils.RsaUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;


@ConfigurationProperties(prefix = "leyou.jwt")
@Data
public class JWTProperties {

    private String secret;

    private String pubKeyPath;

    private String priKeyPath;

    private Integer expire;

    private PublicKey publicKey;

    private PrivateKey privateKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTProperties.class);

    @PostConstruct
    public void init() {
        try {
            File pubKey = new File(pubKeyPath);
            File priKey = new File(priKeyPath);
            if (!pubKey.exists() || !priKey.exists()) {
                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
            }

            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);

        } catch (Exception e) {
            LOGGER.error("初始化公钥与私钥失败！", e);
            throw new RuntimeException();
        }
    }
}
