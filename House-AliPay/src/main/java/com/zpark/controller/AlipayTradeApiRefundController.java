package com.zpark.controller;


import com.zpark.entity.RentalRefund;
import com.zpark.service.IRentalRefundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alipay")
@Tag(name = "支付宝退款接口", description = "支付宝退款接口")
@Slf4j
public class AlipayTradeApiRefundController {

    @Autowired
    private IRentalRefundService rentalRefundService;
    @PostMapping("/refund")
    @Operation(description = "支付宝退款接口")
    public String refund(@RequestBody RentalRefund refund) throws Exception {
        return  rentalRefundService.refund(refund);
    }
}
