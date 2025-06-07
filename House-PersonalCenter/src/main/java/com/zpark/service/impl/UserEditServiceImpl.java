package com.zpark.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zpark.entity.ChangePasswordCondition;
import com.zpark.entity.User;
import com.zpark.mapper.IUserEditMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zpark.service.IUserEditService;
import com.zpark.service.JwtUserDetailsService;
import com.zpark.utils.GetUserFromToken;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author XMC
 * @since 2025-04-28
 */
@Service
public class UserEditServiceImpl extends ServiceImpl<IUserEditMapper, User> implements IUserEditService {

    @Resource
    private GetUserFromToken getUserFromToken;
    @Resource
    private IUserEditMapper iUserEditMapper;
    @Resource
    private JwtUserDetailsService jwtUserDetailsService;

    @Override
    public boolean updateUser(User user, HttpServletRequest request) {
        //token中获取用户
        User userFromToken = getUserFromToken.getUser(request);
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getId, userFromToken.getId());
        int row = iUserEditMapper.update(user,lambdaQueryWrapper);
        if (row < 0) return false;
        //清理缓存里的旧用户名
        jwtUserDetailsService.evictUserCache(userFromToken.getUsername());
        return true;
    }

    @Override
    public int changePassword(ChangePasswordCondition passwordCondition, HttpServletRequest request) {
        User userFromToken = getUserFromToken.getUser(request);
                //密码加密
        String encryptedPassword = DigestUtil.sha256Hex(passwordCondition.getOldPassword());
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getId, userFromToken.getId());
        lambdaQueryWrapper.eq(User::getPassword, encryptedPassword);
        User dbUser = iUserEditMapper.selectOne(lambdaQueryWrapper);
        if (dbUser == null){
            return 1001;
        }else {
            if (!passwordCondition.getNewPassword().equals(passwordCondition.getConfirmNewPassword())){
                return 1002;
            }
            if (passwordCondition.getNewPassword().equals(passwordCondition.getOldPassword())){
                return 1003;
            }
            dbUser.setId(userFromToken.getId());
            dbUser.setPassword(DigestUtil.sha256Hex(passwordCondition.getNewPassword()));
            int row = iUserEditMapper.updateById(dbUser);
            if (row < 1){
                return 1004;
            }
            //清理缓存里的旧密码
            jwtUserDetailsService.evictUserCache(encryptedPassword);
            return 0;
        }
    }
}
