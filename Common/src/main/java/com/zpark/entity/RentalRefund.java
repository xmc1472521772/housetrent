package com.zpark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "租房退款记录实体")
@TableName("rental_refund")
@Slf4j
public class RentalRefund {

    @Schema(description = "退款ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableId(type = IdType.ASSIGN_ID)
    private Long refundId;

    @Schema(description = "退款单号", example = "REF20230815001", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableField(value = "refund_no")
    private String refundNo;

    @Schema(description = "关联订单ID", example = "1001")
    @TableField(value = "order_id")
    private Long orderId;

    @Schema(description = "关联订单编号", example = "ORD20230001")
    @TableField(value = "order_no")
    private String orderNo;

    @Schema(description = "关联支付宝交易号", example = "TRADE20230815001")
    @TableField(value = "trade_no")
    private String tradeNo;

    @Schema(description = "租客ID", example = "1001")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "房东ID", example = "2001")
    @TableField(value = "landlord_id")
    private Long landlordId;

    @Schema(description = "房源ID", example = "3001")
    @TableField(value = "property_id")
    private Long propertyId;

    @Schema(description = "申请退款金额", example = "5000.00")
    @TableField(value = "apply_amount")
    private BigDecimal applyAmount;

    @Schema(description = "实际退款金额", example = "4500.00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal actualAmount;

    @Schema(description = "扣除金额", example = "500.00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal deductAmount;

    @Schema(description = "退款类型(1押金,2租金,3服务费,4其他)", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer refundType;

    @Schema(description = "退款状态(0申请中,1审核通过,2拒绝,3处理中,4成功,5失败)", example = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer refundStatus;

    @Schema(description = "退款原因", example = "提前退租", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String refundReason;

    @Schema(description = "退款备注", example = "工作调动需要提前退租", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String refundRemark;

    @Schema(description = "退款凭证(图片URL)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String refundEvidence;

    @Schema(description = "审核人ID", example = "4001", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long auditorId;

    @Schema(description = "审核时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime auditTime;

    @Schema(description = "审核意见", example = "同意退款，扣除500元作为违约金", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String auditComment;

    @Schema(description = "退款方式(1原路退回,2银行卡,3支付宝,4微信)", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer refundMethod;

    @Schema(description = "退款账户信息", example = "alipay:138****1234", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String refundAccount;

    @Schema(description = "退款交易号", example = "REF20230815001", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String refundTradeNo;

    @Schema(description = "退款完成时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime refundTime;

    @Schema(description = "退款失败原因", example = "银行账户信息错误", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String refundFailReason;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime createdAt;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime updatedAt;
}