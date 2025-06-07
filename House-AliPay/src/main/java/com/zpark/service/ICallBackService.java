package com.zpark.service;

import jakarta.servlet.http.HttpServletRequest;

public interface ICallBackService {
    // 异步回调接口
    String payNotify(HttpServletRequest request) throws Exception;

    // 同步回调接口
    String paySync(HttpServletRequest request) throws Exception;
}
