package com.zpark.service.impl;

import com.zpark.service.ICallBackService;
import com.zpark.service.IOrderService;
import com.zpark.util.SignVerified;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CallBackServiceImpl implements ICallBackService {

    @Autowired
    private SignVerified signVerified;

    @Autowired
    private IOrderService orderService;
    @Override
    public String payNotify(HttpServletRequest request) throws Exception {
        log.info("开始执行异步调用");
        /* 请根据商户自己项目逻辑处理，并返回success或者fail。*/
        if(signVerified.mysignVerified(request)) {//验证成功
            //获取支付宝的通知返回参数，可参考技术文档中服务器异步通知参数列表，如out_trade_no
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //验签成功，更改支付状态为已支付: 0未支付，1已支付
            if (orderService.ChangePaymentStatus(1, out_trade_no, trade_no)){
                orderService.InsertTradeNo(trade_no, out_trade_no);
                log.info("结束执行异步调用");
                return "success";
            }
            log.info("结束执行异步调用");
            return "fail";
        }else {//验证失败
            log.info("结束执行异步调用");
            return "fail";
        }
    }

    @Override
    public String paySync(HttpServletRequest request) throws Exception {
        log.info("开始执行同步调用");
        if(signVerified.mysignVerified(request)){
            log.info("结束执行同步调用");
            return "success";
        }else {
            log.info("结束执行同步调用");
            return "fail";
        }
    }
}
