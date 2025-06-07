package com.zpark.controller;

import com.zpark.entity.Collection;
import com.zpark.service.ICollectionAddAndDeleteService;
import com.zpark.utils.ResponseResult;
import com.zpark.utils.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;


//找房页面收藏接口
@RestController
@RequestMapping("/collection")
@Tag(name = "收藏接口", description = "收藏接口")
public class CollectionController {

    @Resource
    private ICollectionAddAndDeleteService collectionService;

    //收藏房源
    @PostMapping("/add/{houseId}")
    @Operation(summary = "收藏房源", description = "根据房源id收藏房源")
    @Parameters({
            @Parameter(name = "houseId", description = "房源id", required = true),
            @Parameter(name = "Authorization", description = "认证令牌", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string", example = "Bearer XXXXXXX"))
    })
    public ResponseResult<Collection> add(@PathVariable("houseId") Integer houseId, HttpServletRequest request) {
        int row = collectionService.add(houseId, request);
        if(row == 1001) return new ResponseResult<>(StatusCode.FAILURE.value, "该房源不存在", null);
        if(row == 1002) return new ResponseResult<>(StatusCode.FAILURE.value, "该用户不存在", null);
        if(row == 1003 || row == 1004) return new ResponseResult<>(StatusCode.FAILURE.value, "收藏失败，请稍后再试", null);
        return new ResponseResult<>(StatusCode.SUCCESS.value, "收藏成功", null);
    }

    //取消收藏
    @PutMapping("/del/{houseId}")
    @Operation  (summary = "取消收藏", description = "根据房源id取消收藏")
    @Parameters({
            @Parameter(name = "houseId", description = "房源id", required = true),
            @Parameter(name = "Authorization", description = "认证令牌", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string", example = "Bearer XXXXXXX"))
    })
    public ResponseResult<Collection> update(@PathVariable("houseId") Integer houseId, HttpServletRequest request) {
        int row = collectionService.delete(houseId, request);
        if (row == 1001) return new ResponseResult<>(StatusCode.FAILURE.value, "取消收藏失败，请稍后再试", null);
        return new ResponseResult<>(StatusCode.SUCCESS.value, "取消收藏成功", null);
    }
}
