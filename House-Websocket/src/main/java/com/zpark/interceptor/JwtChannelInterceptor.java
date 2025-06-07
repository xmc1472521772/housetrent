package com.zpark.interceptor;

import com.zpark.service.JwtUserDetailsService;
import com.zpark.utils.JwtUtil;
import com.zpark.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final JwtUserDetailsService userDetailsService;

    @Autowired
    private RedisUtil redisUtil;

    public JwtChannelInterceptor(JwtUtil jwtUtil, JwtUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        }

        // 1. 对于 CONNECT 帧的处理
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // 优先尝试复用 HTTP 认证（需确保 SecurityContext 已跨线程传播）
            Authentication httpAuth = SecurityContextHolder.getContext().getAuthentication();
            if (httpAuth != null && httpAuth.isAuthenticated()) {
                accessor.setUser(httpAuth);
                return message;
            }

            // 独立 JWT 验证
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return null; // 拒绝无令牌连接
            }

            try {
                token = token.substring(7);
                String username = jwtUtil.getUserFromToken(token).getUsername();
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 验证令牌并刷新 Redis TTL
                if (jwtUtil.validateToken(token, userDetails)) {
                    redisUtil.setttl(username, 300);
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    accessor.setUser(auth);
                } else {
                    return null; // 令牌无效
                }
            } catch (Exception e) {
                return null; // 拒绝异常连接
            }
        }

        // 2. 对于其他帧（如 SUBSCRIBE/SEND），检查是否已认证
        else if (accessor.getUser() == null) {
            return null; // 拒绝未认证的操作
        }

        return message;
    }
}
