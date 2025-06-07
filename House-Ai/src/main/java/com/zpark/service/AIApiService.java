package com.zpark.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class AIApiService {

    @Value("${api.endpoint}")
    private String apiEndpoint;

    @Value("${api.api-key}")
    private String apiKey;

    @Value("${api.api-secret}")
    private String apiSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 调用AI API的通用方法
     * @param requestBody 请求体（JSON格式）
     * @return API响应结果
     */
    public String callAIApi(Map<String, Object> requestBody) {
        // 构造请求头（如果需要）
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer hmAwmaIPvbzZzMCcGkVC:RRPGkvhYPBVzwFrvLISL");
//        if (apiKey != null) {
//            headers.set("Authorization", "Bearer " + apiKey);
//        }

        // 构造HTTP请求
        HttpEntity request = new HttpEntity(requestBody, headers);

        try {
            // 发送POST请求
            // 改为接收 byte[] 类型响应体
            ResponseEntity<byte[]> response = restTemplate.postForEntity(apiEndpoint, request, byte[].class);
            // 手动以 UTF-8 解码
            return new String(response.getBody(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            // 处理异常（如日志记录）
            throw new RuntimeException("调用AI API失败: " + e.getMessage());
        }
    }
}
