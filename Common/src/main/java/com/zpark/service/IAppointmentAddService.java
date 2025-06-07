package com.zpark.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zpark.entity.Appointment;

/**
 * <p>
 * 预约表 服务类
 * </p>
 *
 * @author XMC
 * @since 2025-04-27
 */
public interface IAppointmentAddService extends IService<Appointment> {
    int save(Appointment appointment, Long id, Long userId);

}
