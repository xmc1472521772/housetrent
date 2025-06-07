package com.zpark.config;

import com.zpark.service.SHA256HexPasswordEncoder;
import com.zpark.utils.PermitPathMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(proxyTargetClass = true)
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final PermitPathMatcher permitPathMatcher;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, PermitPathMatcher permitPathMatcher) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.permitPathMatcher = permitPathMatcher;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        List<String> permitPaths = List.of("/user/**","/ws/**","/v3/**",
                "/alipay/**",
                "/doc.html",               // Knife4j主页
                "/doc.html#/**",
                "/doc.html/**",
                "/webjars/**",             // Knife4j静态资源
                "/v3/api-docs/**",          // OpenAPI JSON文档
                "/swagger-ui/**",           // Swagger UI
                "/swagger-ui.html",         // Swagger UI旧版路径
                "/swagger-resources/**",     // Swagger资源
                "/favicon.ico",             // 图标
                "/error"); // 示例放行路径

        // 注册放行路径
        permitPathMatcher.addPermitPaths(permitPaths);

        // 开始配置HTTP安全规则
        http
                // 禁用CSRF防护（因为使用JWT无状态认证，不需要CSRF保护）
                .csrf(csrf -> csrf.disable())

                // 禁用CORS（跨域资源共享），如果API不需要跨域访问则禁用
                // 如果需要跨域，应该配置具体的CORS规则而非完全禁用
//                .cors(cors -> cors.disable())

                // 开始配置请求授权规则
                .authorizeHttpRequests(auth -> auth
                        // 设置白名单路径（permitPaths）允许所有人访问
                        .requestMatchers(permitPaths.toArray(new String[0])).permitAll()
                        // 用户API
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        // 管理员API
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 其他所有请求都需要认证
                        .anyRequest().authenticated()
                )

                // 配置会话管理策略
                .sessionManagement(session -> session
                        // 设置为无状态（STATELESS），不使用HTTP Session
                        // 这是JWT架构的关键配置
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 配置异常处理
                .exceptionHandling(handling -> handling
                        // 设置认证入口点，返回401状态码（替代默认的登录页面跳转）
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )

                // 配置安全HTTP头部
                .headers(headers -> headers
                        // 启用HSTS（HTTP严格传输安全）
                        .httpStrictTransportSecurity(hsts -> hsts
                                // 包含子域名
                                .includeSubDomains(true)
                                // 有效期1年（单位秒）
                                .maxAgeInSeconds(31536000)
                        )

                        // 设置X-Frame-Options为SAMEORIGIN
                        // 防止点击劫持，只允许同源iframe嵌入
                        .frameOptions(frame -> frame.sameOrigin())
                )
                // 防止XSS攻击的头部
                .headers(headers -> headers
                        .xssProtection(xss -> xss
                                .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                        )
                        // 禁用内容嗅探
                        .contentTypeOptions(HeadersConfigurer.ContentTypeOptionsConfig::disable)
                );


        // 在UsernamePasswordAuthenticationFilter之前添加JWT过滤器
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // 构建并返回安全过滤器链
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SHA256HexPasswordEncoder();
    }


}


