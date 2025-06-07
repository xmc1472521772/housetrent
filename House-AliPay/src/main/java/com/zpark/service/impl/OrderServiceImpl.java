package com.zpark.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zpark.entity.Order;
import com.zpark.entity.User;
import com.zpark.mapper.IOrderMapper;
import com.zpark.service.IOrderService;
import com.zpark.utils.GetUserFromToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
//  订单服务实现类
public class OrderServiceImpl extends ServiceImpl<IOrderMapper, Order> implements IOrderService {
    @Autowired
    private IOrderMapper orderMapper;

    @Autowired
    private GetUserFromToken getUserFromToken;

    @Override
    @Transactional
    //更改订单状态
    public boolean ChangePaymentStatus(Integer paymentStatus, String orderNo, String tradeNo) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        Order order = new Order();
        order.setPaymentStatus(paymentStatus);
        if (tradeNo.isBlank() ||  orderNo.isBlank() ||  paymentStatus == null){
            throw new RuntimeException("参数错误");
        }
        if (!orderNo.isBlank()){
            queryWrapper.eq(Order::getOrderNo, orderNo);
            return orderMapper.update(order, queryWrapper) > 0;
        }
        if (!tradeNo.isBlank()){
            queryWrapper.eq(Order::getTradeNo, tradeNo);
            queryWrapper.eq(Order::getOrderStatus, paymentStatus);
            return orderMapper.update(order, queryWrapper) > 0;
        }
        return false;
    }

    @Override
    @Transactional
    //插入支付宝交易号
    public boolean InsertTradeNo(String tradeNo, String orderNo) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getOrderNo, orderNo);
        Order order = new Order();
        order.setTradeNo(tradeNo);
        return orderMapper.update(order, queryWrapper) > 0;
    }

    @Override
    @Transactional
    //查询所有订单
    public List<Order> getAll(HttpServletRequest request) {
        try {
            User user = getUserFromToken.getUser(request);
            if (user == null) {
                throw new RuntimeException("用户未找到");
            }

            log.info("开始查询用户{}的订单", user.getId());

            LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Order::getTenantId, user.getId());

            List<Order> orders = orderMapper.selectList(queryWrapper);
            if (orders == null) {
                orders = Collections.emptyList();
            }

            log.info("查询到{}条订单记录", orders.size());
            return orders;

        } catch (Exception e) {
            log.error("获取订单列表失败", e);
            throw new RuntimeException("获取订单列表失败", e);
        }
    }
}
