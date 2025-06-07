package com.zpark.service.impl;

import com.zpark.entity.House;
import com.zpark.mapper.HouseMapper;
import com.zpark.service.IHouseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 房源信息表 服务实现类
 * </p>
 *
 * @author fufuking
 * @since 2025-04-25
 */
@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements IHouseService {

    @Autowired
    HouseMapper houseMapper;


    @Override
    public Integer housePublish(House house) {
        return houseMapper.housePublish(house);
    }
}
