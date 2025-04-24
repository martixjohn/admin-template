package com.example.demo.service.user;

import com.example.demo.common.pojo.service.User;

import java.util.Optional;
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
     * @return 用户数据，找不到为空
     */
    Optional<User> getFullInfoByUsername(String username);

    /**
     * 通过角色role查询权限permission
     *
     * @param role 角色
     * @return permissions
     */
    Set<String> getPermissionsByRole(String role);

    /**
     * 登录
     * @description 失败抛出异常
     * @param username 用户名
     * @param password 密码
     */
    void login(String username, String password);

    /**
     * 注册
     * @description 失败抛出异常
     * @param username 用户名
     * @param password 密码
     *
     */
    void register(String username, String password);

    /**
     * 用户名是否合法
     * @param username 用户名
     * @return 是否合法
     */
    boolean validateUsername(String username);

    /**
     * 密码是否合法
     * @param password 密码
     * @return 是否合法
     */
    boolean validatePassword(String password);
}
