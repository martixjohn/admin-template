package com.example.demo.service.user;

import com.example.demo.common.pojo.service.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/**
 * @author martix
 * @description
 * @time 2025/4/25
 */
@Transactional(rollbackFor = Exception.class)
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
     * 登录
     *
     * @param username   用户名
     * @param password   密码
     * @description 失败抛出异常
     */
    void login(String username, String password);

//    /**
//     * 登出当前用户
//     *
//     * @description 失败抛出异常
//     */
//    void logout();

    /**
     * 注册
     *
     * @param username 用户名
     * @param password 密码
     * @param role     角色，不带前缀如ROLE_
     * @description 失败抛出异常
     */
    void register(String username, String password, String role);

    /**
     * 改变密码
     *
     * @param newPassword 新密码
     */
    void changePassword(String newPassword);

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
}
