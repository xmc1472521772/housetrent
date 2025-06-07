package com.zpark.utils;

/**
 * 响应状态码
 */
public enum StatusCode {
	SUCCESS(200, "操作成功"),
	FAILURE(400, "操作失败"),
	UNKONWN(500, "服务器未知错误"),
	SAVE_FAILURE(501, "保存失败"),
	UPDATE_FAILURE(502, "修改失败"),
	DELETE_FAILURE(503, "删除失败"),
	SELECT_FAILURE(504, "查询失败");

	public Integer value;
	public String message;

	StatusCode(Integer value, String message) {
		this.value = value;
		this.message = message;
	}
}
