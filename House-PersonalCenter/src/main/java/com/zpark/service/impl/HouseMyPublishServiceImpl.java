package com.zpark.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zpark.entity.House;
import com.zpark.entity.User;
import com.zpark.mapper.IHouseMyPublishMapper;
import com.zpark.service.IHouseMyPublishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zpark.utils.GetUserFromToken;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 房源信息表 服务实现类
 * </p>
 *
 * @author XMC
 * @since 2025-04-28
 */
@Service
public class HouseMyPublishServiceImpl extends ServiceImpl<IHouseMyPublishMapper, House> implements IHouseMyPublishService {

    @Resource
    private GetUserFromToken getUserFromToken;

    @Resource
    private IHouseMyPublishMapper houseMyPublishMapper;


    @Override
    public Page<House> findList(Integer currentPage, HttpServletRequest request) {
        User user = getUserFromToken.getUser(request);
        LambdaQueryWrapper<House> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(House::getOwnerId, user.getId());
        Page<House> housePage = new Page<>(currentPage, 5);
        return houseMyPublishMapper.selectPage(housePage, lambdaQueryWrapper);
    }
}
