package com.zpark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 房源信息表
 * </p>
 *
 * @author fufuking
 * @since 2025-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("house")
@ApiModel(value="House对象", description="房源信息表")
public class House implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "房源 ID", hidden = true)
    @TableId(value = "house_id", type = IdType.AUTO)
    private Integer houseId;

    @ApiModelProperty(value = "房东 ID")
    @TableField("owner_id")
    private Long ownerId;

    @ApiModelProperty(value = " 标题")
    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @ApiModelProperty(value = "房屋地址")
    @Schema(description = "房屋地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;

    @ApiModelProperty(value = "房屋面积")
    @Schema(description = "房屋面积", requiredMode = Schema.RequiredMode.REQUIRED)
    private Float area;

    @ApiModelProperty(value = "房间数")
    @Schema(description = "房间数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer roomNumber;

    @ApiModelProperty(value = "户型")
    @Schema(description = "户型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roomType;

    @ApiModelProperty(value = "租金")
    @Schema(description = "租金", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal rent;

    @ApiModelProperty(value = "房屋的纬度坐标，用于地图定位")
    private Float latitude;

    @ApiModelProperty(value = "房屋的经度坐标，用于地图定位")
    private Float longitude;

    @ApiModelProperty(value = "房屋状态（1-待租，2-已租）")
    @Schema(description = "房屋状态（1-待租，2-已租）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    @ApiModelProperty(value = "地区")
    @Schema(description = "地区", requiredMode = Schema.RequiredMode.REQUIRED)
    private String region;

    @ApiModelProperty(value = "房源描述")
    @Schema(description = "房源描述", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @ApiModelProperty(value = "图片")
    private String img;

    @ApiModelProperty(value = "视频")
    private String video;

    @ApiModelProperty(value = "是否电梯")
    @Schema(description = "是否电梯", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer elevator;

    @ApiModelProperty(value = "详细地址")
    @Schema(description = "详细地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String detailedAddress;


}
