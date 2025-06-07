package com.zpark.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zpark.entity.*;
import com.zpark.service.*;
import com.zpark.utils.ResponseResult;
import com.zpark.utils.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/personal_center")
@Tag(name = "个人中心接口", description = "个人中心所需的接口")
public class PersonalCenterController {
    @Autowired
    private IAppointmentMyService appointmentMyService;
    @Autowired
    private ICollectionMyService iCollectionMyService;
    @Autowired
    private IHouseMyPublishService iHouseMyPublishService;
    @Autowired
    private IUserEditService iUserEditService;

//    我发布的房源
    @GetMapping("/my_house")
    @Operation(summary = "我发布的房源", description = "分页查询我发布的房源")
    @Parameters({
            @Parameter(name = "currentPage", description = "当前页码"),
            @Parameter(name = "Authorization", description = "认证令牌", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string", example = "Bearer XXXXXXX"))
    })
    public ResponseResult<Page<House>> house(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                             HttpServletRequest request){
        Page<House> housePage = iHouseMyPublishService.findList(currentPage, request);
        return new ResponseResult<>(StatusCode.SUCCESS.value, "查询成功", housePage);
    }


    @GetMapping("/my_appointment")
    @Operation(summary = "我预约的房源", description = "分页查询我预约的房源")
    @Parameters({
            @Parameter(name = "currentPage", description = "当前页码"),
            @Parameter(name = "Authorization", description = "认证令牌", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string", example = "Bearer XXXXXXX"))
    })
    public ResponseResult<Page<Appointment>> appointment(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                                      HttpServletRequest request){
        Page<Appointment> appointmentPage = appointmentMyService.findAll(currentPage, request);
        return new ResponseResult<>(StatusCode.SUCCESS.value, "查询成功", appointmentPage);
    }


//    我的收藏
    @GetMapping("/my_collection")
    @Operation(summary = "我的收藏", description = "分页查询我的收藏")
    @Parameters({
            @Parameter(name = "currentPage", description = "当前页码"),
            @Parameter(name = "Authorization", description = "认证令牌", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string", example = "Bearer XXXXXXX"))
    })
    public ResponseResult<Page<Collection>> collection(@RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage,
            HttpServletRequest request){
        Page<Collection> collectionPage = iCollectionMyService.findAll(currentPage, request);
        return new ResponseResult<>(StatusCode.SUCCESS.value, "查询成功", collectionPage);
   }

   //修改个人资料
    @PutMapping("/update")
    @Operation(summary = "修改个人资料", description = "修改个人资料")
    @Parameters({
            @Parameter(name = "user", description = "用户信息", required = true),
            @Parameter(name = "Authorization", description = "认证令牌", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string", example = "Bearer XXXXXXX"))
    })
    public ResponseResult<User> update(@RequestBody User user, HttpServletRequest request){
        boolean  row = iUserEditService.updateUser(user, request);
        if (!row){
            return new ResponseResult<>(StatusCode.FAILURE.value, "修改失败，请稍后再试", null);
        }
        return new ResponseResult<>(StatusCode.SUCCESS.value, "修改成功, 请重新登陆", user);
    }

    //修改密码
    @PutMapping("/change_password")
    @Operation(summary = "修改密码", description = "修改密码")
    @Parameters({
            @Parameter(name = "Authorization", description = "认证令牌", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string", example = "Bearer XXXXXXX"))
    })
    public ResponseResult<User> changePassword(@RequestBody ChangePasswordCondition user, HttpServletRequest request){
        int row = iUserEditService.changePassword(user, request);
        if (row == 1001){
            return new ResponseResult<>(StatusCode.FAILURE.value, "原密码错误", null);
        }
        if (row == 1002){
            return new ResponseResult<>(StatusCode.FAILURE.value, "两次密码不一致", null);
        }
        if (row == 1003){
            return new ResponseResult<>(StatusCode.FAILURE.value, "新密码与原密码一致", null);
        }
        return new ResponseResult<>(StatusCode.SUCCESS.value, "修改密码成功，请重新登陆", null);
    }

    //取消预约
    @PutMapping("/cancel_appointment")
    @Operation(summary = "取消预约", description = "取消预约")
    @Parameters({
            @Parameter(name = "appointment", description = "预约信息", required = true),
            @Parameter(name = "Authorization", description = "认证令牌", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string", example = "Bearer XXXXXXX"))
    })
    public ResponseResult<Appointment> cancelAppointment(@RequestBody Appointment appointment){
        appointment.setStatus(0);
        boolean row = appointmentMyService.updateById(appointment);
        if (!row){
            return new ResponseResult<>(StatusCode.FAILURE.value, "取消预约失败", null);
        }
        return new ResponseResult<>(StatusCode.SUCCESS.value, "取消预约成功", null);
    }
}
