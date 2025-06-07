package com.zpark.controller;

import com.zpark.service.AIApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")

public class AIController {

    @Autowired
    private AIApiService aiApiService;

    /**
     * 示例接口：传递用户输入，调用AI API并返回结果
     * @param input 用户输入文本
     * @return AI处理后的结果
     */

    /**
     * 调用示例
     * body中只需要一个text参数即可
     * {
     *     "text":"你好"
     * }
     **/
    @PostMapping("/process")

    public ResponseEntity processInput(@RequestBody Map input) {
        String userInput = (String) input.get("text");
        if (userInput == null || userInput.isEmpty()) {
            return ResponseEntity.badRequest().body("输入不能为空");
        }

        // 构造请求体（根据API要求调整）
//        String requestBody = String.format(
//            "{\"model\": \"x1\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}], \"tools\": [{\"type\": \"web_search\", \"web_search\": {\"enable\": true}}]}",
//            userInput
//        );
        Map<String, Object> requestBody = Map.of(
            "model", "x1",
            "messages", List.of(Map.of("role", "user", "content", userInput)),
            "stream", true
        );

        // 调用AI API
        String result = aiApiService.callAIApi(requestBody);



        // 返回结果
        return ResponseEntity.ok(result);
    }

}
