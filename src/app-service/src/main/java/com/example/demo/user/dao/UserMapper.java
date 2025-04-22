package com.example.demo.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.user.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author martix
 * @description
 * @time 2025/4/21 22:01
 */
public interface UserMapper extends BaseMapper<User> {

}
