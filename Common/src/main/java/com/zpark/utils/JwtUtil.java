package com.zpark.utils;

import cn.hutool.core.lang.UUID;
import com.zpark.entity.CustomUserDetails;
import com.zpark.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static long time = 1000 * 60 * 60 * 24;
    private static final long ACCESS_TOKEN_EXPIRE = 1000 * 60 * 30; // 30分钟
    private static final long REFRESH_TOKEN_EXPIRE = 1000 * 60 * 60 * 24 * 7; // 7天

    @Value("${jwt.secret-key}")
    private String secretString;

    private SecretKey getSecretKey() {
        return new SecretKeySpec(secretString.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    public long getRemainingTime(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.getTime() - System.currentTimeMillis();
    }


    // 生成Access Token
    public  String createAccessToken(User user) {
        Map<String,Object> claims=new HashMap<>();
        claims.put("username",user.getUsername());
        claims.put("userId",user.getId());
        claims.put("role",user.getRole());

        if (!user.getAuthorities().isEmpty()) {
            claims.put("authorities", user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
        }

        String jwtToken = Jwts.builder()
                //header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setClaims(claims)
                //payload
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE))
                .setId(UUID.randomUUID().toString())
                //signature
                .signWith(getSecretKey(),SignatureAlgorithm.HS256)
                .compact();
        return jwtToken;
    }

    // 生成Refresh Token（只包含必要信息）
    public  String createRefreshToken(User user) {
        Map<String,Object> claims=new HashMap<>();
        claims.put("username",user.getUsername());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setId(UUID.randomUUID().toString())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE))
                .signWith(getSecretKey())
                .compact();
    }


    public String createAccessToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // 基础信息
        claims.put("username", userDetails.getUsername());
        claims.put("userId",userDetails.getId());

        // 添加权限信息
        if (!userDetails.getAuthorities().isEmpty()) {
            claims.put("authorities", userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
        }


        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .setId(UUID.randomUUID().toString())
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public  User getUserFromToken(String token){
        Map<String, Object> claims = new HashMap<>();
        claims=getClaimsFromToken(token);
        System.out.println("这里是jwtUtil");
        User user =new User();
        user.setUsername(claims.get("username").toString());
        user.setId(Long.parseLong(claims.get("userId").toString()));
        user.setRole((String) claims.get("role"));
        user.setAuthorities((Collection<? extends GrantedAuthority>) claims.get("authorities"));
        return user;
    }
    public  String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 验证Token是否有效
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody(); // 一次性解析并获取 Claims

            final String username = claims.getSubject();
            final Date expiration = claims.getExpiration();

            return username.equals(userDetails.getUsername())
                    && !expiration.before(new Date()); // 直接检查是否过期
        } catch (JwtException | IllegalArgumentException e) {
            return false; // 捕获 JWT 相关异常（过期、签名无效等）
        }

    }
    public  boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    private  Claims getClaimsFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    public Date getExpirationDateFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }


}
