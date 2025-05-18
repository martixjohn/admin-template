package com.example.demo.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.common.exception.AppServiceException;
import com.example.demo.common.pojo.dto.User;
import com.example.demo.common.pojo.dto.UserProfile;
import com.example.demo.common.request.PageQueryRequest;
import jakarta.annotation.Nullable;

import java.util.Set;

/**
 * 用户服务
 *
 * @author martix
 * @description 登录在AuthenticationService
 * @time 2025/4/25
 */
public interface UserService {

    /**
     * 添加用户指定会话，表示登录
     * @description 达到会话数量上限导致其他登录状态失效
     * @param user 用户
     * @param token    token
     */
    void addUserToSession(User user, String token);

    /**
     * 从会话缓存中获取用户指定会话
     * @param username 用户名
     * @param token TOKEN
     * @return 用户 不存在会话为null
     */
    @Nullable
    User getUserFromSession(String username, String token);

    /**
     * 指定用户的全部会话失效
     * @param username 用户名
     */
    void invalidateAllSessionsOfUser(String username);

    /**
     * 指定用户的某个会话失效
     * @param username 用户名
     * @param token 指定登录TOKEN
     */
    void invalidateOneSessionOfUser(String username, String token);

    /**
     * 检查用户名和密码是否匹配
     *
     * @param username 用户名
     * @param password 密码
     * @return 是否匹配
     */
    boolean checkPassword(String username, String password);

    /**
     * 获取用户所有信息
     *
     * @param username 用户名
     * @return 用户数据，找不到为空，没有脱敏
     * @description 请注意，信息没有脱敏
     */
    @Nullable
    User getOneByUsername(String username);

    /**
     * 批量查询
     *
     * @param pageQueryRequest 查询条件
     * @return 分页
     */
    IPage<User> getBatch(PageQueryRequest pageQueryRequest);

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
     * 删除用户
     * @description 失败抛出异常
     * @param username yhm
     */
    void deleteUser(String username);

    /**
     * 通过指定的username来更新用户
     *
     * @param user 用户数据，取username来查询
     * @description 更新非null字段，非username字段，置空设置为空字符串""
     */
    void updateUserProfileByUsername(UserProfile user);

    /**
     * 改变密码
     *
     * @param username    用户名
     * @param newPassword 新密码
     */

    void changePassword(String username, String newPassword);

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
