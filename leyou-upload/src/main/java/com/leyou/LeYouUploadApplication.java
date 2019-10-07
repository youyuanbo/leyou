package com.leyou;

import com.leyou.upload.config.UploadProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(UploadProperties.class)
public class LeYouUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeYouUploadApplication.class, args);
    }
}
