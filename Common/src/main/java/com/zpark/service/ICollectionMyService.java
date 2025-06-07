package com.zpark.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zpark.entity.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author XMC
 * @since 2025-04-28
 */
public interface ICollectionMyService extends IService<Collection> {
    Page<Collection> findAll(Integer currentPage, HttpServletRequest request);
}
