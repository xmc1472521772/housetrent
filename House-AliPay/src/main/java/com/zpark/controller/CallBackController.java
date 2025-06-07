package com.zpark.controller;

import com.zpark.service.ICallBackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付回调请求接口控制层
 */
@RestController
@Slf4j
@RequestMapping("/alipay/callback")
@Tag(name = "支付宝回调接口",  description = "当订单支付成功将调用异步回调和同步回调，异步回调返回订单相关的详细参数，同步回调用于支付成功后跳转到指定页面")
public class CallBackController {

	@Autowired
	private ICallBackService callBackService;


	/**
	 * 异步回调通知接口
	 */
	@PostMapping("/pay_notify")
	@Operation(description = "支付异步回调接口")
	public String notifyFun(HttpServletRequest request) throws Exception {
		return callBackService.payNotify(request);
	}

	/**
	 * 同步回调通知接口
	 */
	@GetMapping("/pay_sync")
	@Operation(description = "支付同步回调接口, 支付成功后返回的页面")
	public String syncFun(HttpServletRequest request) throws Exception {
		return callBackService.paySync(request);
	}
}
