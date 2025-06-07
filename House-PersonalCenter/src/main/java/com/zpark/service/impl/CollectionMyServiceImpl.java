package com.zpark.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zpark.entity.Collection;
import com.zpark.entity.User;
import com.zpark.mapper.ICollectionMyMapper;
import com.zpark.service.ICollectionMyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zpark.utils.GetUserFromToken;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author XMC
 * @since 2025-04-28
 */
@Service
public class CollectionMyServiceImpl extends ServiceImpl<ICollectionMyMapper, Collection> implements ICollectionMyService {

    @Resource
    private GetUserFromToken getUserFromToken;

    @Resource
    private ICollectionMyMapper collectionMapper;

    @Override
    public Page<Collection> findAll(Integer currentPage, HttpServletRequest request) {
        User user = getUserFromToken.getUser(request);
        LambdaQueryWrapper<Collection> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Collection::getUserId, user.getId());
        lambdaQueryWrapper.eq(Collection::getLogicalDel, 0);
        Page<Collection> collectionPage = new Page<>(currentPage, 5);
        return collectionMapper.selectPage(collectionPage, lambdaQueryWrapper);
    }
}
