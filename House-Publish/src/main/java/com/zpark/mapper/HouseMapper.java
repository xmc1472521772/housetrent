package com.zpark.mapper;

import com.zpark.entity.House;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 房源信息表 Mapper 接口
 * </p>
 *
 * @author fufuking
 * @since 2025-05-13
 */
public interface HouseMapper extends BaseMapper<House> {

    Integer housePublish(House house);

}
