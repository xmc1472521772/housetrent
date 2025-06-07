package com.zpark.controller;


import com.zpark.entity.Order;
import com.zpark.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
@Tag(name = "订单管理")
@Slf4j
public class OrderController {

    @Autowired
    private IOrderService orderService;


    @GetMapping("/get_ords")
    @Operation(description = "查询租房的所有订单")
    public List<Order> getAll(HttpServletRequest request) {
        List<Order> orders = orderService.getAll(request);
        return orders;
    }
}
