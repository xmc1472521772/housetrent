package com.zpark.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zpark.entity.Appointment;
import com.zpark.entity.User;
import com.zpark.mapper.IAppointmentMyMapper;
import com.zpark.service.IAppointmentMyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zpark.utils.GetUserFromToken;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 预约表 服务实现类
 * </p>
 *
 * @author XMC
 * @since 2025-04-28
 */
@Service
public class AppointmentMyServiceImpl extends ServiceImpl<IAppointmentMyMapper, Appointment> implements IAppointmentMyService {
    @Resource
    private GetUserFromToken getUserFromToken;

    @Resource
    private IAppointmentMyMapper iAppointmentMyMapper;
    @Override
    public Page<Appointment> findAll(Integer currentPage, HttpServletRequest request) {
        User user = getUserFromToken.getUser(request);
        LambdaQueryWrapper<Appointment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Appointment::getTenantId, user.getId());
        Page<Appointment> appointmentPage = new Page<>(currentPage, 5);
        return iAppointmentMyMapper.selectPage(appointmentPage, lambdaQueryWrapper);
    }
}
