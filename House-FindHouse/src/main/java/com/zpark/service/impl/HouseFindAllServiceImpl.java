package com.zpark.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zpark.entity.House;
import com.zpark.entity.QueryConditions;
import com.zpark.mapper.IHouseFindAllMapper;
import com.zpark.service.IHouseFindAllService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class HouseFindAllServiceImpl extends ServiceImpl<IHouseFindAllMapper, House> implements IHouseFindAllService {
    @Resource
    private IHouseFindAllMapper houseFindAllMapper;

    @Override
    public Page<House> findAll(Integer currentPage, QueryConditions house) {

        //构建查询条件
        LambdaQueryWrapper<House> queryWrapper = new LambdaQueryWrapper<>();
        if (house == null){
            return houseFindAllMapper.selectPage(new Page<>(currentPage, 9), queryWrapper);
        }
        if (StringUtils.isNotBlank(house.getRegion()) || StringUtils.isNotEmpty(house.getRegion())){
            queryWrapper.likeRight(House::getRegion, house.getRegion());
        }
        if(StringUtils.isNotEmpty(house.getDetailedAddress())){
            queryWrapper.like(House::getDetailedAddress, house.getDetailedAddress());
        }
        if (house.getMinPrice() != null && house.getMaxPrice() != null){
            queryWrapper.between(House::getRent, house.getMinPrice(), house.getMaxPrice());
        }
        if (house.getMinArea() != null && house.getMaxArea() != null){
            queryWrapper.between(House::getArea, house.getMinArea(), house.getMaxArea());
        }
        // 创建分页对象
        Page<House> housePage = new Page<>(currentPage, 9);

        // 执行分页查询
        return houseFindAllMapper.selectPage(housePage, queryWrapper);
    }
}
