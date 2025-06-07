package com.zpark.config;

import com.zpark.service.JwtUserDetailsService;
import com.zpark.utils.JwtUtil;
import com.zpark.utils.PermitPathMatcher;
import com.zpark.utils.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final PermitPathMatcher permitPathMatcher;

    private final JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    public JwtRequestFilter(JwtUserDetailsService jwtUserDetailsService,JwtUtil jwtUtil,PermitPathMatcher permitPathMatcher) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.permitPathMatcher = permitPathMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)throws ServletException, IOException {


        if (permitPathMatcher.matches(request)) {
            chain.doFilter(request, response);
            return;
        }

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // JWT Token格式: "Bearer token". 去掉"Bearer "前缀
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                // 增加令牌黑名单检查
                if (redisUtil.hasKey("auth:blacklist:" + jwtToken)) {
                    sendErrorResponse(response, "Token revoked");
                    return;
                }
                username = jwtUtil.getUserFromToken(jwtToken).getUsername();
//            }
//                 catch (SignatureException ex) {
//                    logger.error("无效的JWT签名");
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "无效令牌");
//                    return;
//                } catch (Exception ex) {
//                logger.error("捕获到异常类型: " + ex.getClass().getName()); // 新增
//                logger.error("无法获取JWT令牌", ex);
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "令牌处理失败");
//                return;
//            }
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token");
                sendErrorResponse(response, "Invalid JWT token");
                return;
            } catch (Exception e) {
                logger.error("JWT Token parsing error", e);
                sendErrorResponse(response, "Invalid JWT token");
                return;
            }
        } else {
            if(requestTokenHeader == null){
                logger.warn("The JWT token does not exist");
                sendErrorResponse(response, "The JWT token does not exist");
                return;
            }
            logger.warn("JWT Token does not begin with Bearer String");
            sendErrorResponse(response, "The JWT token does not exist or JWT Token must begin with Bearer String");
            return;
        }

        // 验证token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
                redisUtil.setttl(username,300);
                if (jwtUtil.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    sendErrorResponse(response, "Invalid JWT token");
                    return;
                }
                
                String redisToken = (String) redisUtil.get("auth:accesstoken:"+username);
                String shatoken= DigestUtils.sha256Hex(jwtToken);
                if(!shatoken.equals(redisToken)){
                    sendErrorResponse(response, "token违法");
                    return ;
                }

            } catch (Exception e) {
                logger.error("JWT Validation error", e);
                sendErrorResponse(response, "Invalid JWT token");
                return;
            }
        }
        chain.doFilter(request, response);
    }




    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}

