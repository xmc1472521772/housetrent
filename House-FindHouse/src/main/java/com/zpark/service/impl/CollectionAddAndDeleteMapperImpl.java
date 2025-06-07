package com.zpark.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zpark.entity.Collection;
import com.zpark.entity.House;
import com.zpark.entity.User;
import com.zpark.mapper.ICollectionAddAndDeleteMapper;
import com.zpark.mapper.IHouseFindAllMapper;
import com.zpark.mapper.IUserBaseMapper;
import com.zpark.service.ICollectionAddAndDeleteService;
import com.zpark.utils.GetUserFromToken;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CollectionAddAndDeleteMapperImpl extends ServiceImpl<ICollectionAddAndDeleteMapper, Collection> implements ICollectionAddAndDeleteService {
    @Resource
    private ICollectionAddAndDeleteMapper collectionMapper;

    @Resource
    private IHouseFindAllMapper houseFindAllMapper;

    @Resource
    private IUserBaseMapper iUserBaseMapper;

    @Resource
    private GetUserFromToken getUserFromToken;

    @Override
    public int add(Integer houseId, HttpServletRequest request) {
        //判断房源是否存在
        House house = houseFindAllMapper.selectById(houseId);
        if (house == null) return 1001;
        //判断用户是否存在
        User user  = iUserBaseMapper.selectById(getUserFromToken.getUser(request).getId());
        System.out.println("user: -------"+user);
        if (user == null) return 1002;

        LambdaQueryWrapper<Collection> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Collection::getUserId, user.getId());
        lambdaQueryWrapper.eq(Collection::getHouseId, houseId);
        Collection collection = new Collection();
        collection.setUserId(user.getId());
        collection.setHouseId(houseId);
        collection.setCreateTime(LocalDateTime.now());
        collection.setLogicalDel(0);

        // 判断数据库是否存在收藏, 如果存在，则更新收藏的逻辑删除状态
        if (collectionMapper.selectOne(lambdaQueryWrapper) != null){
            int row = collectionMapper.update(collection, lambdaQueryWrapper);
            if(row < 1) return 1003;
            else return 0;
        }
        int row  = collectionMapper.insert(collection);
        if(row < 1) return 1004;
        return 0;
    }

    @Override
    public int delete(Integer houseId, HttpServletRequest request) {
        //从token获取用户信息
        User user = getUserFromToken.getUser(request);
        //物理删除 1逻辑删除 0未删除
        Collection collection = new Collection();
        collection.setLogicalDel(1);
        LambdaQueryWrapper<Collection> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Collection::getUserId, user.getId());
        lambdaQueryWrapper.eq(Collection::getHouseId, houseId);
        int row = collectionMapper.update(collection, lambdaQueryWrapper);
        if(row < 1) return 1001;
        return 0;
    }
}
