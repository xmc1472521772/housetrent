package com.zpark.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker  // 启用WebSocket消息代理
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 配置消息代理
        registry.enableStompBrokerRelay("/topic", "/queue")  // 代理目的地前缀
                .setRelayHost("107.173.181.247")  // RabbitMQ主机
                .setRelayPort(61613)  // STOMP端口
                //待改
                .setClientLogin("fufu")  // 客户端登录
                .setClientPasscode("lhj")  // 客户端密码
                //
                .setSystemLogin("fufuking")   // 系统连接用户名
                .setSystemPasscode("lhj445");// 系统连接密码
        // 应用程序目的地前缀
        registry.setApplicationDestinationPrefixes("/app");

        // 用户目的地前缀(点对点通信)
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册STOMP端点
        registry.addEndpoint("/ws")  // WebSocket端点路径
                .setAllowedOriginPatterns("*")  // 允许的源
                .withSockJS();  // 启用SockJS回退选项
    }
}
