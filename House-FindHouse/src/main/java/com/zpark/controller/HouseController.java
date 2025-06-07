package com.zpark.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zpark.entity.House;
import com.zpark.entity.QueryConditions;
import com.zpark.service.IHouseFindAllService;
import com.zpark.utils.ResponseResult;
import com.zpark.utils.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 房源信息表 前端控制器
 * </p>
 *
 * @author XMC
 * @since 2025-04-24
 */
@RestController
@RequestMapping("/house")
@Tag(name = "房源查找接口", description = "房源搜索及房源详细查看")
public class HouseController {

    @Resource
    private IHouseFindAllService houseService;


//    分页查询
    @GetMapping("/find")
    @Operation(summary = "房源查询", description = "分页展示房源")
    @Parameters({
            @Parameter(name = "currentPage", description = "当前页码"),
            @Parameter(name = "house", description = "查询条件"),
            @Parameter(
                    name = "Authorization",
                    description = "认证令牌",
                    required = true,
                    in = ParameterIn.HEADER,
                    schema = @Schema(type = "string", example = "Bearer XXXXXXX")
            )
    })
    public ResponseResult<Page<House>> findAll(@RequestParam(value = "currentPage", defaultValue = "1")  Integer currentPage,
                                               @RequestBody(required = false) QueryConditions house
    ){
        Page<House> houses = houseService.findAll(currentPage, house);
        return new ResponseResult<>(StatusCode.SUCCESS.value, "查询成功", houses);
    }


    @GetMapping("/detail/{id}")
    @Operation(summary = "房源详情", description = "根据房源id查询房源详情")
    @Parameter(name = "id", description = "房源id", required = true)
    public ResponseResult<House> findById(@PathVariable(value = "id") Integer id){
        House house = houseService.getById(id);
        if (house == null) return new ResponseResult<>(StatusCode.FAILURE.value, "查询失败，该房源不存在", null);
        return new ResponseResult<>(StatusCode.SUCCESS.value, "查询成功", house);
    }

}

