package com.zpark.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zpark.entity.Appointment;
import com.zpark.entity.House;
import com.zpark.mapper.IAppointmentAddMapper;
import com.zpark.mapper.IHouseFindAllMapper;
import com.zpark.service.IAppointmentAddService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AppointmentAddServiceImpl extends ServiceImpl<IAppointmentAddMapper, Appointment> implements IAppointmentAddService {
    @Resource
    private IHouseFindAllMapper iHouseFindAllMapper;

    @Resource
    private IAppointmentAddMapper iAppointmentAddMapper;
    @Override
    public int save(Appointment appointment, Long houseId, Long userId) {
        LambdaQueryWrapper<House> queryWrapperHouse = new LambdaQueryWrapper<>();
        queryWrapperHouse.eq(House::getHouseId, houseId);
        House house = iHouseFindAllMapper.selectOne(queryWrapperHouse);
        if (house == null){
            return 1001;
        }

//        判断是否已经预约过了
        LambdaQueryWrapper<Appointment>  queryWrapperAppointment = new LambdaQueryWrapper<>();
        queryWrapperAppointment.eq(Appointment::getHouseId, houseId);
        queryWrapperAppointment.eq(Appointment::getTenantId, userId);
        queryWrapperAppointment.ne(Appointment::getStatus, 0);
        if (iAppointmentAddMapper.selectOne(queryWrapperAppointment) != null){
            return 1002;
        }
//        appointment.setAppointmentId(appointment.getAppointmentId());
        appointment.setTenantId(userId);
        appointment.setCreateTime(LocalDateTime.now());
        appointment.setHouseId(houseId);
        appointment.setOwnerId(house.getOwnerId());
        appointment.setStatus(2);
        return iAppointmentAddMapper.insert(appointment);
    }
}
