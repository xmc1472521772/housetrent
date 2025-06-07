package com.zpark.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zpark.entity.User;
import com.zpark.mapper.IUserBaseMapper;
import com.zpark.service.IUserBaseService;
import org.springframework.stereotype.Service;

@Service
public class UserBaseServiceImpl extends ServiceImpl<IUserBaseMapper, User> implements IUserBaseService {
}
