package com.zpark.utils;

import com.zpark.entity.User;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;


@Component
// 获取用户信息
public class GetUserFromToken {

    @Resource
    private JwtUtil jwtUtil;
    // 从token中获取用户信息
    public User getUser(HttpServletRequest request){
        String requestTokenHeader = request.getHeader("Authorization");
        String token=null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")){
            token=requestTokenHeader.substring(7);
        }
        return jwtUtil.getUserFromToken(token);
    }

}
