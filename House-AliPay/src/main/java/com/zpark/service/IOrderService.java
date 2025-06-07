package com.zpark.service;

import com.zpark.entity.Order;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigInteger;
import java.util.List;

public interface IOrderService {
    boolean ChangePaymentStatus(Integer paymentStatus, String tradeNo, String orderNo);

    boolean InsertTradeNo(String tradeNo, String orderNo);

    List<Order> getAll(HttpServletRequest request);
}
