package com.zpark.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.io.Serializable;

/**
 * 支付配置类
 */
@Data
@Configuration
public class AlipayConfig extends CertAlipayRequest implements Serializable {
	// 沙箱appid
	@Value("${alipay.appid}")
	public String appid;

	// 私钥 pkcs8格式的
	@Value("${alipay.rsa_private_key}")
	public String rsaPrivateKey;

	// 请求网关  固定
	@Value("${alipay.url}")
	public String url;

	//异步通知地址，完成支付后通知商户支付结果地址
	@Value("${alipay.notify_url}")
	public String payNotifyUrl;

	//同步地址、用户完成支付后跳转的页面url
	@Value("${alipay.return_url}")
	public String payReturnUrl;
	// 编码
	@Value("${alipay.charset}")
	public String charset;

	// 返回格式
	@Value("${alipay.format}")
	public String format;

	// 支付宝公钥
	@Value("${alipay.alipay_public_key}")
	public String alipayPublicKey;

	// 沙箱支付宝公钥
	@Value("${alipay.zhifu_public_key}")
	public String zhiFuPublicKey;

	// RSA2
	@Value("${alipay.signtype}")
	public String signType;


	public AlipayClient getAlipayClient() {
		// 创建支付宝客户端
		return new DefaultAlipayClient(
				this.getUrl(),
				this.getAppid(),
				this.getRsaPrivateKey(),
				this.getFormat(),
				this.getCharset(),
				this.getZhiFuPublicKey(),
				this.getSignType());
	}
}
