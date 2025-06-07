package com.zpark.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.zpark.entity.User;
import com.zpark.mapper.UserMapper;
import com.zpark.service.IUserService;
import com.zpark.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xx
 * @since 2025-03-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;




    @Override
    public Long selectId(User user){
        return userMapper.selectId(user);
    }

    @Override
    public String password(String password) {
        return DigestUtil.sha256Hex(password);
    }

    @Override
    public int selectuser(User user) {
        if (userMapper.selectuser(user) != 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String register(User user) {

        String value = (String) redisUtil.get("auth:emailcode:"+user.getEmail());
        if(!user.getCode().equals(value)){
            return "验证码错误";
        }

        if (selectuser(user) == 0) {
            user.setPassword(password(user.getPassword()));
            if (userMapper.register(user) != 0) {
                    redisUtil.delete(user.getEmail());
                return "注册成功";
            } else {
                return "发生错误，注册失败";
            }
        }
        return "用户名或邮箱已存在";
    }



    @Override
    public String resendemail(User user) {


        com.resend.Resend resend = new com.resend.Resend("re_YnL8XeYe_2E7nTASWUP9rvh1xQqFSyD46");
        String code = String.valueOf((int)((Math.random()*9+1)* Math.pow(10,5)));

        redisUtil.setWithPrefix("auth:emailcode:",user.getEmail(),code,30, TimeUnit.MINUTES);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("fufu@fufuking.top")
                .to(user.getEmail())
                //.to("lihuijije545@gmail.com")
                .subject("HouseSysTem 验证码")
                .html("这是你的验证码,有效期为30分钟<br>"+
                      "<strong>"+code+"</strong>"+"<br/>"+"<strong>"+code+"</strong>"+"<br/>"+"<strong>"+code+"</strong>"+"<br/>"+
                      "若你正在尝试进行非法行为，就此收手，没人会受伤。<br/>"+"否则，<br/>"+
                      "我将，点燃星海"
                     )
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println(data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }

        return code;
    }




    //xie
    @Override
    public User login(User user) {
        //System.out.println(user.getPassword());
        user.setPassword(password(user.getPassword()));
       // System.out.println(user.getPassword());
        return userMapper.login(user);
    }

}