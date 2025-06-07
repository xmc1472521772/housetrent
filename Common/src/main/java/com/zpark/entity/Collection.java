package com.zpark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author XMC
 * @since 2025-04-28
 */
@Getter
@Setter
@TableName("collection")
@Schema(name = "Collection请求实体", description = "收藏房源所需参数")
public class Collection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收藏id主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "收藏id主键", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    /**
     * 收藏者的id
     */
    @TableField("user_id")
    @Schema(description = "收藏者的id", accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    /**
     * 收藏房源的id
     */
    @TableField("house_id")
    @Schema(description = "收藏房源的id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer houseId;

    /**
     * 收藏时间
     */
    @TableField("create_time")
    @Schema(description = "收藏时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createTime;

    /**
     * 逻辑删除，0代表没有删除，1代表删除
     */
    @TableField("logical_del")
    @Schema(description = "逻辑删除，0代表没有删除，1代表删除", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer logicalDel;


}
