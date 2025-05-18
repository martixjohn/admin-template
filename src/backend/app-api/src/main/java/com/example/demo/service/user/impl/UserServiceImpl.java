package com.example.demo.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.config.security.AppSecurityConfigProperties;
import com.example.demo.common.exception.AppServiceException;
import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.common.manager.RedisCacheManager;
import com.example.demo.common.pojo.dto.CachedUser;
import com.example.demo.common.pojo.dto.User;
import com.example.demo.common.pojo.dto.UserProfile;
import com.example.demo.common.pojo.po.RolePO;
import com.example.demo.common.pojo.po.UserPO;
import com.example.demo.common.pojo.po.UserRolePO;
import com.example.demo.common.request.PageQueryRequest;
import com.example.demo.repository.user.RoleMapper;
import com.example.demo.repository.user.RolePermissionMapper;
import com.example.demo.repository.user.UserMapper;
import com.example.demo.repository.user.UserRoleMapper;
import com.example.demo.service.user.UserService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author martix
 * @description
 * @time 2025/4/21 22:02
 */
@Service
@Setter
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements UserService, UserDetailsService {

    // 用户名匹配
    private Pattern USERNAME_PATTERN;
    // 密码匹配
    private Pattern PASSWORD_PATTERN;
    // 会话失效时间
    private Duration SESSION_EXPIRE_TIME;
    // 允许的最大会话数量
    private int MAX_SESSION_COUNT;


    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisCacheManager redisCacheManager;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;

    @Autowired
    private void setAppSecurityProperties(AppSecurityConfigProperties appSecurityConfigProperties) {
        AppSecurityConfigProperties.Account account = appSecurityConfigProperties.getAccount();
        String usernamePattern = account.getUsernamePattern();
        String passwordPattern = account.getPasswordPattern();
        log.debug("配置用户名规则: {}", usernamePattern);
        log.debug("配置密码规则: {}", passwordPattern);
        this.USERNAME_PATTERN = Pattern.compile(usernamePattern);
        this.PASSWORD_PATTERN = Pattern.compile(passwordPattern);
        this.SESSION_EXPIRE_TIME = appSecurityConfigProperties.getSession().getExpiresIn();
        this.MAX_SESSION_COUNT = appSecurityConfigProperties.getSession().getMaximumSessions();
    }

    private String getCachedUserKey(String username) {
        return "USER:" + username;
    }

    @Override
    public void addUserToSession(User user, String token) {
        String username = user.getUsername();
        String cacheKey = getCachedUserKey(username);
        // 针对同一个用户
        synchronized (cacheKey) {
            // 缓存的用户
            CachedUser cachedUser = redisCacheManager.get(cacheKey, CachedUser.class);
            // 已经有缓存
            if (cachedUser != null) {
                // 达到会话上限, 删除第一个 (LRU)
                if (cachedUser.getTokens().size() == MAX_SESSION_COUNT) {
                    cachedUser.getTokens().removeFirst();
                    log.info("{}达到会话上限，强制让之前会话下线", cachedUser.getUser().getUsername());
                }
            } else {
                // 会话过期后第一次登录
                cachedUser = new CachedUser();
                cachedUser.setTokens(new LinkedList<>());
            }

            // 存储JWT,用户信息缓存到redis
            cachedUser.getTokens().addLast(token);
            // 为了一致性,防止有可能是旧数据
            cachedUser.setUser(user);

            // 设置缓存, 并刷新过期时间
            redisCacheManager.set(cacheKey, cachedUser, SESSION_EXPIRE_TIME);
        }

        userMapper.updateLastLoginTime(username);
    }


    @Nullable
    @Override
    public User getUserFromSession(String username, String token) {
        CachedUser cachedUser = redisCacheManager.get(getCachedUserKey(username), CachedUser.class);

        // 视为过期
        if (cachedUser == null || !cachedUser.getTokens().contains(token)) {
            return null;
        }

        return cachedUser.getUser();
    }

    @Override
    public void invalidateAllSessionsOfUser(String username) {
        redisCacheManager.delete(getCachedUserKey(username));
    }

    @Override
    public void invalidateOneSessionOfUser(String username, String token) {
        synchronized (username.intern()) {
            String cacheKey = getCachedUserKey(username);
            CachedUser cachedUser = redisCacheManager.get(cacheKey, CachedUser.class);
            if (cachedUser == null) return;// 已经登出

            // 所有会话都登出, 没必要存储
            if (cachedUser.getTokens().size() == 1) {
                redisCacheManager.delete(cacheKey);
                return;
            }

            Duration expiredIn = redisCacheManager.getExpire(cacheKey);

            if (!expiredIn.isZero()) {
                // 只删除当前登录状态(可能不同地点登录)
                cachedUser.getTokens().remove(token);
                redisCacheManager.set(cacheKey, cachedUser, expiredIn);
            }
        }
    }

    @Override
    public boolean checkPassword(String username, String password) {
        UserPO userPO = userMapper.selectOne(Wrappers.<UserPO>lambdaQuery().eq(UserPO::getUsername, username));
        if (userPO == null) {
            return false;
        }
        return passwordEncoder.matches(password, userPO.getPassword());
    }

    @Nullable
    @Override
    public User getOneByUsername(String username) {
        return userMapper.selectOneByUsername(username);
    }

    // 条件查询限制的field
    private final static Set<String> QUERY_CONDITION_LIMIT_FIELDS = Set.of("username", "createdTime", "updatedTime");

    @Override
    public IPage<User> getBatch(PageQueryRequest pageQueryRequest) {

        // 注意，无法一次性查询出角色和权限
        IPage<UserPO> userPOPage = userMapper.selectPage(
                pageQueryRequest.toPage(),
                pageQueryRequest.toQueryConditions(QUERY_CONDITION_LIMIT_FIELDS));

        List<User> list = userPOPage.getRecords().stream().map(
                e -> getOneByUsername(e.getUsername())
        ).toList();
        Page<User> ret = new Page<>(userPOPage.getCurrent(), userPOPage.getSize(), userPOPage.getTotal());
        BeanUtil.copyProperties(userPOPage, ret);
        ret.setRecords(list);
        return ret;
    }


    @Override
    public Set<String> getPermissionsByRole(String role) {
        return rolePermissionMapper.selectPermissionsByRoleName(role);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUser(String username, String password, String role) {
        checkValidUsernameAndPassword(username, password);

        boolean present = getOneByUsername(username) != null;
        if (present) {
            throw new AppServiceException(ExceptionCode.BAD_REQUEST, "用户已存在");
        }
        RolePO rolePO = roleMapper.selectByRoleName(role);
        if (rolePO == null) {
            throw new AppServiceException(ExceptionCode.BAD_REQUEST, "非法的角色名");
        }

        // 插入用户
        String encodedPassword = passwordEncoder.encode(password);
        UserPO userPO = new UserPO();
        userPO.setUsername(username);
        userPO.setPassword(encodedPassword);
        userMapper.insert(userPO);

        // 插入关联表
        UserRolePO userRolePO = new UserRolePO();
        userRolePO.setUserId(userPO.getId());
        userRolePO.setRoleId(rolePO.getId());
        userRoleMapper.insert(userRolePO);
    }

    @Override
    public void deleteUser(String username) {
        if (!userMapper.deleteByUsername(username)) {
            throw new AppServiceException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updateUserProfileByUsername(UserProfile user) {
        // 查询操作
        UserPO userPO = userMapper.selectOnePOByUsername(user.getUsername());
        // 不存在
        if (userPO == null) {
            throw new AppServiceException(ExceptionCode.BAD_REQUEST, "用户不存在");
        }

        // 更改非null字段
        Optional.ofNullable(user.getNickname()).ifPresent(userPO::setNickname);
        Optional.ofNullable(user.getEmail()).ifPresent(userPO::setEmail);
        Optional.ofNullable(user.getAvatarLocalPath()).ifPresent(userPO::setAvatarLocalPath);
        Optional.ofNullable(user.getAvatarServerPath()).ifPresent(userPO::setAvatarServerPath);

        userMapper.updateByUsername(userPO);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changePassword(String username, String newPassword) {
        checkValidUsernameAndPassword(username, newPassword);

        UserPO userPO = userMapper.selectOne(Wrappers.<UserPO>lambdaQuery().eq(UserPO::getUsername, username));
        // 设置新密码
        userPO.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(userPO);
    }

    @Override
    public boolean validateUsername(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    @Override
    public boolean validatePassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }


    // 检查用户名密码合法性，非法抛出异常
    @Override
    public void checkValidUsernameAndPassword(String username, String password) {

        final String usernameMsg = "用户名不符合规则" + USERNAME_PATTERN;
        final String passwordMsg = "密码不符合规则" + PASSWORD_PATTERN;
        if (!validateUsername(username)) {
            throw new AppServiceException(ExceptionCode.BAD_REQUEST, usernameMsg);
        }
        if (!validatePassword(password)) {
            throw new AppServiceException(ExceptionCode.BAD_REQUEST, passwordMsg);
        }
    }

    /**
     * Spring Security内部调用
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        log.info("loadUserByUsername: {}", username);
        User fullInfoByUsername = getOneByUsername(username);
        if (fullInfoByUsername == null) {
            throw new UsernameNotFoundException("用户 " + username + " 找不到");
        }
        return fullInfoByUsername;
    }

}
