package com.zpark.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


    /**
     * 作用：标记该类为全局异常处理器
     * 引用者：Spring MVC框架在启动时会扫描所有带有@ControllerAdvice的类
     * 触发时机：当控制器抛出异常时，Spring会查找匹配的异常处理方法
    */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 处理文件存储异常
     */
    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<Object> handleFileStorageException(
            FileStorageException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Date());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "File Storage Error");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理无效文件异常
     */
    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<Object> handleInvalidFileException(
            InvalidFileException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Date());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Invalid File");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

//    /**
//     * 处理文件大小超过限制异常
//     */
//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    public ResponseEntity<Object> handleMaxSizeException(
//            MaxUploadSizeExceededException ex, HttpServletRequest request) {
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("timestamp", new Date());
//        response.put("status", HttpStatus.PAYLOAD_TOO_LARGE.value());
//        response.put("error", "File Too Large");
//        response.put("message", "File size exceeds the allowed limit");
//        response.put("path", request.getRequestURI());
//
//        return new ResponseEntity<>(response, HttpStatus.PAYLOAD_TOO_LARGE);
//    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(
            Exception ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Date());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("message", "An unexpected error occurred");
        response.put("path", request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}