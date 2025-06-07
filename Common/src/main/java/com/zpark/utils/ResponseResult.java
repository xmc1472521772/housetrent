package com.zpark.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 后端统一响应结果类型
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseResult<T> implements Serializable {
	// 响应状态码
	private Integer code;
	// 响应状态消息
	private String message;
	// 响应数据
	private T jsonData;
}
