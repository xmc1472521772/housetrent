package com.zpark.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zpark.mapper.UserMapper_Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserMapper_Common userMapper;


    public JwtUserDetailsService(UserMapper_Common userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    @Cacheable(value = "auth:user",key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.zpark.entity.User user = userMapper.selectOne(
                new QueryWrapper<com.zpark.entity.User>().eq("username", username));

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    @CacheEvict(value = "auth:user",key = "#username")
    public void evictUserCache(String username) {
        System.out.println("清除");
    }


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;



    public String evictAllUserCache() {
        Set<String> keys = redisTemplate.keys("auth:user*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
        return "清除所有 auth:user 缓存";
    }
}
