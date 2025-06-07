package com.zpark.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zpark.entity.Appointment;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 * 预约表 服务类
 * </p>
 *
 * @author XMC
 * @since 2025-04-28
 */
public interface IAppointmentMyService extends IService<Appointment> {
    Page<Appointment> findAll(Integer currentPage, HttpServletRequest request);
}
