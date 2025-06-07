package com.zpark.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/process") // 针对你的 /process 接口
                .allowedOrigins("*") // 允许的来源
                .allowedMethods("POST", "GET", "OPTIONS") // 允许的方法
                .allowedHeaders("Content-Type") // 允许的头部
                .allowCredentials(false) // 是否允许凭据
                .maxAge(3600); // 预检请求的缓存时间
    }
}