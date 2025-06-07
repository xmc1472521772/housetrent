package com.zpark.util;

import com.alipay.api.internal.util.AlipaySignature;
import com.zpark.config.AlipayConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@Slf4j
public class SignVerified{
    @Autowired
    private AlipayConfig alipayConfig;
    public boolean mysignVerified(HttpServletRequest request) throws Exception {
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //异步验签：切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //公钥证书模式验签，alipayPublicCertPath是支付宝公钥证书引用路径地址，需在对应应用中下载
        //boolean signVerified= AlipaySignature.rsaCertCheckV1(params, alipayPublicCertPath, charset,sign_type);
        //普通公钥模式验签，切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        return AlipaySignature.rsaCheckV1(params, alipayConfig.getZhiFuPublicKey(), alipayConfig.getCharset(), alipayConfig.getSignType());
    }
}
