package com.zpark.service;

import com.alipay.api.AlipayApiException;
import com.zpark.entity.Order;

/**
 * 预下单业务层接口
 */
public interface IPayService {

	/**
	 * 创建订单，生成用户访问支付宝的跳转链接
	 * 模拟创建订单操作，订单参数固定写死
	 * @return 生成用户访问支付宝的跳转链接
	 */
	String createOrder2(Order order) throws AlipayApiException;

}
