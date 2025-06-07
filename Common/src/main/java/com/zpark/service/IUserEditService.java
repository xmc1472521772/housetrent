package com.zpark.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zpark.entity.ChangePasswordCondition;
import com.zpark.entity.User;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author XMC
 * @since 2025-04-28
 */
public interface IUserEditService extends IService<User> {
    boolean updateUser(User user, HttpServletRequest request);
    int changePassword(ChangePasswordCondition user, HttpServletRequest request);
}
