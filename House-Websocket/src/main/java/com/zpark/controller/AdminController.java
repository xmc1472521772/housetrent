package com.zpark.controller;

import com.zpark.service.JwtUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Tag(name = "清除服务器登录缓存接口", description = "清楚服务器登录缓存接口")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;


    @PostMapping("/evict-cache")
    @Operation(
            summary = "清除登录缓存",
            description = "强制清除所有用户的 JWT 登录缓存（通常用于紧急情况或缓存更新）",
            security = @SecurityRequirement(name = "Bearer Authentication")  // 表明需要认证
    )
    @Parameters({
            @Parameter(
                    name = "Authorization",
                    description = "认证令牌",
                    required = true,
                    in = ParameterIn.HEADER,
                    schema = @Schema(type = "string", example = "Bearer xxx.yyy.zzz")
            )
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "缓存清除成功",
                    content = @Content(schema = @Schema(example = "User cache evicted successfully"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "缓存清除失败",
                    content = @Content(schema = @Schema(example = "Failed to evict cache: 错误详情"))
            )
    })
    public ResponseEntity<?> evictUserCache() {
        try {
            logger.info("Admin requested to evict user cache");
            String result = jwtUserDetailsService.evictAllUserCache();
            logger.info("User cache evicted successfully");
            return ResponseEntity.ok().body("User cache evicted successfully");
        } catch (CacheEvictionException e) {
            logger.error("Failed to evict user cache: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to evict cache: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during cache eviction: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }



    // 定义自定义异常
    public static class CacheEvictionException extends RuntimeException {
        public CacheEvictionException(String message) {
            super(message);
        }
    }


}