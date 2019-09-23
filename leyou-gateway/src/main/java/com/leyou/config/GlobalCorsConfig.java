package com.leyou.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsFilter corsFilter() {

        // 1. 添加CORS配置信息
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1.1 允许的域名，不要写“*”,否则cookie就无法使用
        corsConfiguration.addAllowedOrigin("http://manager.leyou.com");
        // 1.2 是否发送Cookie信息
        corsConfiguration.setAllowCredentials(true);
        // 1.3 允许的请求方式
        corsConfiguration.addAllowedMethod("OPTIONS");
        corsConfiguration.addAllowedMethod("HEAD");
        corsConfiguration.addAllowedMethod("GET");
        corsConfiguration.addAllowedMethod("PUT");
        corsConfiguration.addAllowedMethod("POST");
        corsConfiguration.addAllowedMethod("DELETE");
        corsConfiguration.addAllowedMethod("PATCH");
        // 1.4 允许的头信息
        corsConfiguration.addAllowedHeader("*");
        // 1.5 有效时长（在有效时长内，如果再次发送特殊请求，不会发送预请求）
        corsConfiguration.setMaxAge(3600L);
        // 2. 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        // 3. 返回CorsFilter
        return new CorsFilter(urlBasedCorsConfigurationSource);

    }
}
