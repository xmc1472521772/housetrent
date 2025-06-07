package com.zpark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 预约表
 * </p>
 *
 * @author XMC
 * @since 2025-04-27
 */
@Getter
@Setter
@TableName("appointment")
@Schema(name = "Appointment请求实体", description = "预约房源所需参数")
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预约ID
     */
    @TableId(value = "appointment_id", type = IdType.ASSIGN_ID)
    @Schema(description = "预约ID", accessMode = Schema.AccessMode.READ_ONLY)
    private Long appointmentId;

    /**
     * 关联的租客ID
     */
    @TableField("tenant_id")
    @Schema(description = "关联的租客ID", accessMode = Schema.AccessMode.READ_ONLY)
    private Long tenantId;

    /**
     * 关联的房源ID
     */
    @TableField("house_id")
    @Schema(description = "关联的房源ID", accessMode = Schema.AccessMode.READ_ONLY)
    private Long houseId;

    /**
     * 预约时间
     */
    @TableField("date")
    @Schema(description = "预约时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-04-27")
    private LocalDate date;

    /**
     * 状态 (0-已取消, 1-已确认, 2-待处理)
     */
    @TableField("status")
    @Schema(description = "状态 (0-已取消, 1-已确认, 2-待处理)", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer status;

    @TableField("message")
    @Schema(description = "预约留言", requiredMode = Schema.RequiredMode.REQUIRED, example = "你好明天下午有时间吗")
    private String message;

    @TableField("create_time")
    @Schema(description = "预约时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createTime;

    @TableField("owner_id")
    @Schema(description = "房东 ID", accessMode = Schema.AccessMode.READ_ONLY)
    private Long ownerId;

    @TableField("phone")
    @Schema(description = "预约人电话", requiredMode = Schema.RequiredMode.REQUIRED, example = "15692712421")
    private Long phone;

    @TableField("name")
    @Schema(description = "预约人姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "谢先生")
    private String name;
}
