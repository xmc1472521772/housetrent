package com.zpark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "订单实体")
@TableName("rental_orders")
public class Order {

    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableId(value = "order_id", type = IdType.ASSIGN_ID)
    private Long orderId;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderNo;

    @Schema(description = "支付宝交易号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String tradeNo;

    @Schema (description = "订单标题", example = "花溪大学城公寓", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderTitle;

    @Schema(description = "租客ID", example = "1001", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long tenantId;

    @Schema(description = "房东ID", example = "2001", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long landlordId;

    @Schema(description = "房源ID", example = "3001", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long propertyId;

    @Schema(description = "订单类型(1长租,2短租)", example = "1")
    private Integer orderType;

    @Schema(description = "月租金", example = "5000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal rentAmount;

    @Schema(description = "押金金额", example = "10000.00")
    private BigDecimal depositAmount;

    @Schema(description = "平台服务费", example = "500.00")
    private BigDecimal serviceFee;

    @Schema(description = "订单总金额", example = "15500.00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal totalAmount;

    @Schema(description = "支付状态(0未支付,1部分支付,2已支付)", example = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer paymentStatus;

    @Schema(description = "租赁开始日期", example = "2023-01-01")
    private LocalDate leaseStartDate;

    @Schema(description = "租赁结束日期", example = "2024-01-01")
    private LocalDate leaseEndDate;

    @Schema(description = "签约日期", example = "2023-01-01T10:00:00")
    private LocalDateTime signingDate;

    @Schema(description = "订单状态(0待确认,1已确认,2已入住,3已完成,4已取消)", example = "0")
    private Integer orderStatus;

    @Schema(description = "取消原因", example = "个人原因取消")
    private String cancelReason;

    @Schema(description = "付款周期(1月付,2季付,3半年付,4年付)", example = "1")
    private Integer paymentCycle;

    @Schema(description = "佣金比例", example = "0.05")
    private BigDecimal commissionRate;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime createdAt;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;
}