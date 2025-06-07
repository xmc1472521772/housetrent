package com.zpark.config;

import com.zpark.interceptor.JwtChannelInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)  // 设置高优先级
public class WebSocketSecurityConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtChannelInterceptor jwtChannelInterceptor;

    public WebSocketSecurityConfig(JwtChannelInterceptor jwtChannelInterceptor) {
        this.jwtChannelInterceptor = jwtChannelInterceptor;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // 添加JWT拦截器到WebSocket通道
        registration.interceptors(jwtChannelInterceptor);
    }
}
