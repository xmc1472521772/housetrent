package com.zpark.controller;

import com.alipay.api.AlipayApiException;
import com.zpark.entity.Order;
import com.zpark.service.IPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;


@RestController
@RequestMapping("/alipay")
@Slf4j
@Tag(name = "支付宝支付接口", description = "返回支付宝付款链接，前端直接跳转链接即可")
public class AlipayController {

	@Autowired
	private IPayService payService;

	@PostMapping("/create_order")
	@Operation(description = "创建订单, 返回一个支付链接，前端得到链接打开即可支付")
	@Parameter(name = "Authorization", description = "认证令牌", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string", example = "Bearer XXXXXXX"))
	public String createOrder(@RequestBody Order order) throws AlipayApiException {
		// 调用业务层方法
		return payService.createOrder2(order);
	}
}
