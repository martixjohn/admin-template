package com.example.demo.user.service;

import com.example.demo.user.pojo.service.User;

import java.util.List;
import java.util.Set;

/**
 * @author martix
 * @description
 * @time 2025/4/21 22:01
 */
public interface UserService {

    /**
     * 获取用户所有信息，包含权限信息
     *
     * @param username 用户名
     * @return 用户数据
     */
    User getFullInfoByUsername(String username);

    /**
     * 通过角色role查询权限permission
     *
     * @param role 角色
     * @return permissions
     */
    Set<String> getPermissionsByRole(String role);

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return token字符串，非null
     * @throws AppServiceException 登陆失败
     */
    String login(String username, String password);


}
