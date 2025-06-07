package com.zpark.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zpark.entity.House;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 * 房源信息表 服务类
 * </p>
 *
 * @author XMC
 * @since 2025-04-28
 */
public interface IHouseMyPublishService extends IService<House> {
    Page<House> findList(Integer currentPage ,HttpServletRequest request);
}
