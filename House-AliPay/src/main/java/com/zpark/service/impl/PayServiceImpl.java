package com.zpark.service.impl;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zpark.config.AlipayConfig;
import com.zpark.entity.Order;
import com.zpark.mapper.IOrderMapper;
import com.zpark.service.IPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 预下单接口实现类
 */
@Service
@Slf4j
// 预下单服务实现类
public class PayServiceImpl extends ServiceImpl<IOrderMapper, Order> implements IPayService {
	 @Autowired
	 private AlipayConfig  alipayConfig;

	 @Autowired
	 private IOrderMapper orderMapper;
	@Override
	@Transactional
	public String createOrder2(Order order) throws AlipayApiException {
		// 初始化SDK
		AlipayClient alipayClient = alipayConfig.getAlipayClient();

		// 构造请求参数以调用接口
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setNotifyUrl( alipayConfig.getPayNotifyUrl());
		request.setReturnUrl(alipayConfig.getPayReturnUrl());
		AlipayTradePagePayModel model = new AlipayTradePagePayModel();

		// 设置商户订单号ORD+时间戳+4位随机数
		model.setOutTradeNo("ORD"+System.currentTimeMillis()+ RandomStringUtils.randomNumeric(4));

		// 设置订单总金额
		model.setTotalAmount(order.getTotalAmount().toString());

		// 设置订单标题
		model.setSubject(order.getOrderTitle());

		// 设置产品码
		model.setProductCode("FAST_INSTANT_TRADE_PAY");

		// 设置PC扫码支付的方式
		model.setQrPayMode("1");

		// 设置商户自定义二维码宽度
		model.setQrcodeWidth(100L);

		//设置订单超时时间为30分钟
		model.setTimeoutExpress("30m");

		request.setBizModel(model);
		// 第三方代调用模式下请设置app_auth_token
		// request.putOtherTextParam("app_auth_token", "<-- 请填写应用授权令牌 -->");

		//AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "POST");
		// 如果需要返回GET请求，请使用
		AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "GET");
		String pageRedirectionData = response.getBody();
		System.out.println(pageRedirectionData);

		if (response.isSuccess()) {
			System.out.println("调用成功");
			//将创建的订单保存到数据库
			order.setOrderNo(model.getOutTradeNo());
			if (orderMapper.insert(order) > 0){
				return pageRedirectionData;
			}
			throw new RuntimeException("创建订单失败");
		} else {
			System.out.println("调用失败");
			// sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
			// String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
			// System.out.println(diagnosisUrl);
			return "调用失败";
		}
	}


}
