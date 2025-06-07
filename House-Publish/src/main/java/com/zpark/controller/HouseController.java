package com.zpark.controller;


import com.zpark.entity.House;

import com.zpark.result.PublishResult;
import com.zpark.service.IHouseService;
import com.zpark.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 房源信息表 前端控制器
 * </p>
 *
 * @author fufuking
 * @since 2025-04-25
 */
@RestController
@RequestMapping("/house")
public class HouseController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IHouseService houseService;

    @PutMapping("/housePublish")
    public PublishResult housePublish(@RequestBody House house,HttpServletRequest request){
        String requestTokenHeader = request.getHeader("Authorization");
        String token=null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")){
            token=requestTokenHeader.substring(7);
        }

        house.setOwnerId(jwtUtil.getUserFromToken(token).getId());



        if(houseService.housePublish(house)==1){
            return new PublishResult().publishResult(1,"添加成功");
        }

        return new PublishResult().publishResult(0,"添加失败");
    }

}
