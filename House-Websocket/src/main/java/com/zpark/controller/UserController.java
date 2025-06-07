package com.zpark.controller;


import com.zpark.entity.User;
import com.zpark.result.UserResult;
import com.zpark.service.IUserService;
import com.zpark.utils.JwtUtil;
import com.zpark.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xx
 * @since 2025-03-13
 */
@RestController
@RequestMapping("/user1")
public class UserController {


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IUserService userService;

    //xie
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/get")
    public User getex(@RequestBody String token){
        System.out.println("Received token: " + token);
        token = token.substring(7);
        System.out.println("Received token: " + token);
        return jwtUtil.getUserFromToken(token);
    }

//    登录 传入username，password
    @PostMapping("/login")
    public UserResult Login(@RequestBody User user){

        User u = userService.login(user);
        if (u == null){
            return new UserResult().loginandregister(0,"用户名或密码错误");
        }
        u.setId(userService.selectId(user));
        //System.out.println("用户"+u);

        return new UserResult().loginandregister(1, "登录成功");
    }
}
