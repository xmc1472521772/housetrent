package com.zpark.service;

import com.zpark.entity.House;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 房源信息表 服务类
 * </p>
 *
 * @author fufuking
 * @since 2025-04-25
 */
public interface IHouseService extends IService<House> {

    Integer housePublish(House house);

}
