package com.example.demo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.user.dao.UserMapper;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.user.pojo.entity.User;

/**
 * @author martix
 * @description
 * @time 2025/4/21 22:02
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getById(int id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery().select(User::getUsername);
        return getBaseMapper().selectOne(queryWrapper);
    }
}
