package com.zpark.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "房源查询条件对象", description = "房源查询条件")
public class QueryConditions implements Serializable {

//   地区
    @Schema(description = "地区")
    private String region;
//   详细地址
    @Schema(description = "详细地址")
    private String detailedAddress;
//   最小价格
    @Schema(description = "最小价格")
    private Integer minPrice;
//   最大价格
    @Schema(description = "最大价格")
    private Integer maxPrice;
//   最小面积
    @Schema(description = "最小面积")
    private Integer minArea;
//    最大面积
    @Schema(description = "最大面积")
    private Integer maxArea;

}
