package com.zpark.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zpark.entity.Collection;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author XMC
 * @since 2025-04-28
 */
public interface ICollectionAddAndDeleteService extends IService<Collection> {
    int add(Integer houseId, HttpServletRequest request);

    int delete(Integer houseId, HttpServletRequest request);
}
