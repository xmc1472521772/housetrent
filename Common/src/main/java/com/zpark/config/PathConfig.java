package com.zpark.config;

import com.zpark.utils.PermitPathMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathConfig {

    @Bean
    public PermitPathMatcher permitPathMatcher() {
        return new PermitPathMatcher();
    }
}
