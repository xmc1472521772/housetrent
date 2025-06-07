package com.zpark.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zpark.entity.House;
import com.zpark.entity.QueryConditions;

/**
 * <p>
 * 房源信息表 服务类
 * </p>
 *
 * @author XMC
 * @since 2025-04-24
 */
public interface IHouseFindAllService extends IService<House> {
    Page<House> findAll(Integer page, QueryConditions house);
}
