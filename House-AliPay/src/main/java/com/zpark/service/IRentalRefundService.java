package com.zpark.service;

import com.alipay.api.AlipayApiException;
import com.zpark.entity.RentalRefund;

public interface IRentalRefundService {


    String refund(RentalRefund refund) throws Exception;

    String alipayTradeFastpayRefundQuery(RentalRefund refund) throws AlipayApiException;

    boolean changeRefundStatus(Integer refundStatus, String tradeNo, String orderNo, String refundNo);
}
