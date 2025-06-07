package com.zpark.controller;

import com.zpark.dto.loginDto;
import com.zpark.dto.registerDto;
import com.zpark.dto.sendemailDto;
import com.zpark.entity.JwtResponse;
import com.zpark.entity.User;
import com.zpark.result.UserResult;
import com.zpark.service.IUserService;
import com.zpark.service.JwtUserDetailsService;
import com.zpark.utils.JwtUtil;
import com.zpark.utils.RedisUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Tag(name = "认证处理中心接口", description = "认证处理中心接口")
public class AuthController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;


    //发送验证码 传入username，email
    @PostMapping("/resendemail")
    @Operation(
            summary = "发送验证码",
            description = "向用户邮箱发送新的验证码（用于注册）"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "发送成功",
                    content = @Content(schema = @Schema(implementation = UserResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "发送失败（邮箱无效或服务异常）",
                    content = @Content(schema = @Schema(example = "{\"code\":400,\"message\":\"发送失败\"}"))
            )
    })
    public UserResult resendemail(@RequestBody  @Valid sendemailDto sendemailDto){
        User user=new User();
        user.setUsername(sendemailDto.getUsername());
        user.setEmail(sendemailDto.getEmail());
        if(!userService.resendemail(user).isEmpty()){
            return new UserResult().loginandregister(200,"发送成功");
        }else{
            return  new UserResult().loginandregister(400,"发送失败，请稍后重试");
        }

    }

    //注册 传入username，password，email，emailcode
    @PostMapping("/register")
    @Operation(
            summary = "用户注册",
            description = "通过邮箱验证码完成用户注册"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "注册成功",
                    content = @Content(schema = @Schema(example = "{\"code\":200,\"message\":\"注册成功\"}"))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "注册失败（验证码错误/用户已存在等）",
                    content = @Content(schema = @Schema(example = "{\"code\":400,\"message\":\"邮箱已被注册\"}"))
            )
    })
    public UserResult register(@RequestBody @Valid registerDto registerDto){

        User user=new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setEmail(registerDto.getEmail());
        user.setCode(registerDto.getCode());

        user.setRole("USER");
        String x=userService.register(user);
        if(x.equals("注册成功")){
            return new UserResult().loginandregister(200,x);
        }else {
            return  new UserResult().loginandregister(400,x);
        }
    }


    // 通用用户登录
    @PostMapping("/authenticate")
    @Operation(
            summary = "用户登录",
            description = "通用用户登录接口，返回JWT令牌"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "登录成功",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "登录失败（用户名或密码错误）",
                    content = @Content(schema = @Schema(example = "{\"error\":\"Invalid credentials\"}"))
            )
    })
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @Valid loginDto loginDto) {

        User user=new User();
        user.setUsername(loginDto.getUsername());
        user.setPassword(loginDto.getPassword());

        return processLogin(user, false);
    }

    // 管理员专用登录
    @PostMapping("/admin/authenticate")
    @Operation(
            summary = "管理员登录",
            description = "仅限管理员账号登录",
            security = {}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "登录成功",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "非管理员账号尝试登录",
                    content = @Content(schema = @Schema(example = "{\"error\":\"Access denied\"}"))
            )
    })
    public ResponseEntity<?> createAdminAuthenticationToken(@RequestBody @Valid loginDto loginDto) {

        User user=new User();
        user.setUsername(loginDto.getUsername());
        user.setPassword(loginDto.getPassword());
        return processLogin(user, true);
    }

    // 统一的登录处理逻辑
    private ResponseEntity<?> processLogin(User user, boolean isAdminLogin) {
        try {
            // 1. 认证用户名密码
            authenticate(user.getUsername(), user.getPassword());

            // 2. 获取完整用户信息
            user.setId(userService.selectId(user));
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(user.getUsername());
            user.setAuthorities(userDetails.getAuthorities());
            // 3. 如果是管理员登录，检查角色
            if (isAdminLogin && !hasAdminRole(userDetails)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Access denied: Administrator privileges required");
            }

            if(hasAdminRole(userDetails)){
                user.setRole("ADMIN");
            }else {
                user.setRole("USER");
            }

            // 4. 生成令牌
            final String accessToken = jwtUtil.createAccessToken(user);
            final String refreshToken = jwtUtil.createRefreshToken(user);

            // 5. 存储令牌到Redis
            if (!storeTokens(user.getUsername(), accessToken, refreshToken)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to store token");
            }

            // 6. 更新用户活跃状态
            redisUtil.setttl(user.getUsername(), 300);

            // 7. 构建响应
            ResponseCookie refreshTokenCookie = buildRefreshTokenCookie(refreshToken);

            // 如果是管理员登录，可以在响应中添加额外信息
            if (isAdminLogin) {
                Map<String, Object> response = new HashMap<>();
                response.put("accessToken", accessToken);
                response.put("role", "ADMIN");
                response.put("message", "Admin login successful");

                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                        .body(response);
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                    .body(new JwtResponse(accessToken));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("用户名或密码错误");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("认证失败: " + e.getMessage());
        }
    }





    @PostMapping("/refresh-token")
    @Operation(
            summary = "刷新访问令牌",
            description = "通过有效的Refresh Token获取新的Access Token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "刷新成功",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Refresh Token无效或已过期",
                    content = @Content(schema = @Schema(example = "{\"error\":\"无效的刷新令牌\"}"))
            )
    })
    public ResponseEntity<?> refreshToken(@Parameter(description = "刷新令牌（从Cookie自动携带）", required = true) @CookieValue(name = "refresh_token") String refreshToken) {

        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("无效的刷新令牌");
        }

        try {
            // 1. 验证Refresh Token
            String username = jwtUtil.getUsernameFromToken(refreshToken);

            // 2. 检查Redis中的Refresh Token是否匹配
            String storedRefresh = (String) redisUtil.get("auth:refreshtoken:" + username);

            if (!refreshToken.equals(storedRefresh)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("刷新令牌已失效");
            }

            // 3. 生成新的Access Token
            User user = new User();
            user.setUsername(username);
            user.setId(userService.selectId(user)); // 重新查询用户ID
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(user.getUsername());
            user.setAuthorities(userDetails.getAuthorities());
            if(hasAdminRole(userDetails)){
                user.setRole("ADMIN");
            }else {
                user.setRole("USER");
            }
            String newAccessToken = jwtUtil.createAccessToken(user);

            // 4. 更新Redis中的Access Token
            redisUtil.setWithPrefix("auth:accesstoken:", username, DigestUtils.sha256Hex(newAccessToken), 30, TimeUnit.MINUTES);

            return ResponseEntity.ok(new JwtResponse(newAccessToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("令牌刷新失败: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    @Operation(
            summary = "用户登出",
            description = "使当前用户的Token失效"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "登出成功",
                    content = @Content(schema = @Schema(example = "{\"message\":\"Logout successful\"}"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "服务端异常",
                    content = @Content(schema = @Schema(example = "{\"message\":\"Logout failed\"}"))
            )
    })
    public ResponseEntity<?> logout(
            @Parameter(description = "退出登录")
            @CookieValue(value = "refresh_token") String refreshToken,
            HttpServletRequest request) {

        try {
            // 1. 从请求头获取access token（如果有）
            String authHeader = request.getHeader("Authorization");
            String accessToken = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                accessToken = authHeader.substring(7);
            }

            // 2. 处理refresh token
            if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
                String username = jwtUtil.getUserFromToken(refreshToken).getUsername();

                // 将refresh token加入黑名单（防止在过期前被重复使用）
                redisUtil.setWithPrefix("auth:blacklist:refresh:", refreshToken, "",
                        jwtUtil.getRemainingTime(refreshToken), TimeUnit.MILLISECONDS);

                // 清除相关缓存
                redisUtil.delete("auth:refreshtoken:" + username);
                jwtUserDetailsService.evictUserCache(username);
            }

            // 3. 处理access token（如果有）
            if (accessToken != null && jwtUtil.validateToken(accessToken)) {
                String username = jwtUtil.getUserFromToken(accessToken).getUsername();

                // 将access token加入黑名单（剩余有效期内不可用）
                redisUtil.setWithPrefix("auth:blacklist:access:", accessToken, "",
                        jwtUtil.getRemainingTime(accessToken), TimeUnit.MILLISECONDS);

                redisUtil.delete("auth:accesstoken:" + username);
            }

            // 通知客户端清除Cookie
            ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                    .httpOnly(true)
                    .secure(true)  // 生产环境应该启用
                    .path("/user/refresh-token")
                    .maxAge(0)
                    .sameSite("Strict")  // 防止CSRF
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(Map.of("message", "Logout successful","timestamp", Instant.now().toString()));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Logout failed"));
        }
    }

    // 存储令牌到Redis
    private boolean storeTokens(String username, String accessToken, String refreshToken) {
        boolean accessTokenStored = redisUtil.setWithPrefix("auth:accesstoken:",
                username, DigestUtils.sha256Hex(accessToken), 30, TimeUnit.MINUTES);

        boolean refreshTokenStored = redisUtil.setWithPrefix("auth:refreshtoken:",
                username, refreshToken, 7, TimeUnit.DAYS);

        return accessTokenStored && refreshTokenStored;
    }


    // 构建Refresh Token的HttpOnly Cookie
    private ResponseCookie buildRefreshTokenCookie(String token) {
        return ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(true) // 生产环境启用
                .path("/user/refresh-token")
                .maxAge(7 * 24 * 60 * 60) // 7天
                .sameSite("Strict")
                .build();
    }

    // 认证方法
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    // 检查管理员角色
    private boolean hasAdminRole(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

}




// 辅助DTO类



