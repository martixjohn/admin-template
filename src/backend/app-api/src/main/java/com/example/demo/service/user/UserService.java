package com.example.demo.service.user;

import com.example.demo.common.exception.AppServiceException;
import com.example.demo.common.pojo.service.User;

import java.util.Optional;
import java.util.Set;

/**
 * @author martix
 * @description
 * @time 2025/4/25
 */
public interface UserService {

    /**
     * 获取用户所有信息
     *
     * @param username 用户名
     * @return 用户数据，找不到为空
     * @description 请注意，信息没有脱敏
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
     * 添加用户
     *
     * @param username 用户名
     * @param password 密码
     * @param role     角色，不带前缀如ROLE_
     * @description 失败抛出异常
     */
    void addUser(String username, String password, String role);

    /**
     * 改变密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */

    void changePassword(String oldPassword, String newPassword);

    /**
     * 用户名是否合法
     *
     * @param username 用户名
     * @return 是否合法
     */
    boolean validateUsername(String username);

    /**
     * 密码是否合法
     *
     * @param password 密码
     * @return 是否合法
     */
    boolean validatePassword(String password);

    /**
     * 检查用户名密码合法性，非法抛出异常
     *
     * @param username 用户名
     * @param password 密码
     * @throws AppServiceException 用户名或密码不合法
     */
    void checkValidUsernameAndPassword(String username, String password);
}
