package com.zpark.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zpark.entity.User;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xx
 * @since 2025-03-13
 */
public interface IUserService extends IService<User> {

     String password(String password);
     int selectuser(User user);
     String register(User user);

     Long selectId(User user);

     String resendemail(User user);



     //xie
     User login(User user);
}
