package com.zpark.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zpark.entity.User;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xx
 * @since 2025-03-13
 */


public interface UserMapper extends BaseMapper<User> {

    int selectuser(User user);
    int register(User user);



    Long selectId(User user);
    //xie

    User login(User user);
}
