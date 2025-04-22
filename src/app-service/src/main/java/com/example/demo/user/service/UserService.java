package com.example.demo.user.service;

import com.example.demo.user.pojo.entity.User;

/**
 * @author martix
 * @description
 * @time 2025/4/21 22:01
 */
public interface UserService {
    User getById(int id);
    User getByUsername(String username);
}
