package com.example.demo.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.config.security.AppSecurityProperties;
import com.example.demo.common.exception.AppServiceException;
import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.common.pojo.entity.*;
import com.example.demo.common.pojo.service.User;
import com.example.demo.repository.user.*;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;
    private final PasswordEncoder passwordEncoder;
    private final ObjectFactory<AuthenticationManager> authenticationManager;
    private final SecurityContextRepository securityContextRepository;
//    private final List<LogoutHandler> logoutHandlers;
    // 用户名匹配
    private Pattern USERNAME_PATTERN;
    // 密码匹配
    private Pattern PASSWORD_PATTERN;

    @Autowired
    private void setAppSecurityProperties(AppSecurityProperties appSecurityProperties) {
        AppSecurityProperties.Account account = appSecurityProperties.getAccount();
        String usernamePattern = account.getUsernamePattern();
        String passwordPattern = account.getPasswordPattern();
        log.debug("配置用户名规则: {}", usernamePattern);
        log.debug("配置密码规则: {}", passwordPattern);
        this.USERNAME_PATTERN = Pattern.compile(usernamePattern);
        this.PASSWORD_PATTERN = Pattern.compile(passwordPattern);
    }


    @Override
    public Optional<User> getFullInfoByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return Optional.empty();
        }
        User res = new User();
        UserPO userPO = userMapper
                .selectOne(
                        Wrappers.<UserPO>lambdaQuery()
                                .eq(UserPO::getUsername, username)
                );
        if (userPO == null)
            return Optional.empty();
        log.info("数据库中用户信息: {}", userPO);
        // 用户_角色关联表，得到roleId
        Set<Long> roleIds = userRoleMapper
                .selectList(
                        Wrappers.<UserRolePO>lambdaQuery()
                                .eq(UserRolePO::getUserId, userPO.getId())
                ).stream()
                .map(UserRolePO::getRoleId)
                .collect(Collectors.toSet());

        // 每个roleId查询到role name
        Set<String> roleNames = new HashSet<>();
        for (Long roleId : roleIds) {
            RolePO rolePO = roleMapper.selectById(roleId);
            // 重要！
            if (rolePO != null) {
                roleNames.add(rolePO.getName());
            }
        }
        res.setRoles(roleNames);

        // 对每个角色role获取permission
        Set<String> permissionNames = new HashSet<>();
        for (Long rId : roleIds) {
            permissionNames.addAll(getPermissionByRoleId(rId));
        }
        res.setPermissions(permissionNames);

        // 组装结果
        BeanUtil.copyProperties(userPO, res);
        res.setRoles(roleNames);
        res.setPermissions(permissionNames);
        log.info("读取用户信息: {}", res);
        return Optional.of(res);
    }


    @Override
    public Set<String> getPermissionsByRole(String role) {
        RolePO rolePO = roleMapper.selectOne(Wrappers.<RolePO>lambdaQuery().eq(RolePO::getName, role));
        if (rolePO == null) {
            return new HashSet<>(0);
        }
        return getPermissionByRoleId(rolePO.getId());
    }

    @Override
    public void login(String username, String password) {
        checkValidUsernameAndPassword(username, password);
        log.info("登录用户名：{}    密码：{}", username, password);
        Authentication unauthenticated = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        // 内部 loadUserByUsername
        Authentication authentication = authenticationManager.getObject().authenticate(unauthenticated);
        // 此处登录成功
        // 记录登录时间
        userMapper.update(Wrappers.<UserPO>lambdaUpdate()
                .eq(UserPO::getUsername, username)
                .set(UserPO::getLastLoginTime, LocalDateTime.now()));
        // TODO 设置过期时间
        // 内部使用SecurityContextStrategy
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        // 保存
        securityContextRepository.saveContext(context, requestAttributes.getRequest(), requestAttributes.getResponse());
    }

//    @Override
//    public void logout() {
//        log.info("用户尝试登出: {}", SecurityContextHolder.getContext().getAuthentication());
//        for (LogoutHandler logoutHandler : logoutHandlers) {
//            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            logoutHandler.logout(requestAttributes.getRequest(), requestAttributes.getResponse(), authentication);
//        }
//    }

    @Override
    public void register(String username, String password, String role) {
        checkValidUsernameAndPassword(username, password);

        boolean present = getFullInfoByUsername(username).isPresent();
        if (present) {
            throw new AppServiceException(ExceptionCode.BAD_REQUEST, "用户已存在");
        }
        RolePO rolePO = roleMapper.selectOne(Wrappers.<RolePO>lambdaQuery().eq(RolePO::getName, role));
        if (rolePO == null) {
            throw new AppServiceException(ExceptionCode.BAD_REQUEST, "非法的角色名");
        }
        // TODO 校验是否有权限注册

        String encodedPassword = passwordEncoder.encode(password);
        UserPO userPO = new UserPO();
        userPO.setUsername(username);
        userPO.setPassword(encodedPassword);

        userMapper.insert(userPO);
    }

    @Override
    public void changePassword(String newPassword) {
        // TODO 改变密码
    }

    @Override
    public boolean validateUsername(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    @Override
    public boolean validatePassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    private Set<String> getPermissionByRoleId(Long roleId) {
        // 角色_权限关联表，得到permissionId
        List<Long> permissionIds = rolePermissionMapper
                .selectList(Wrappers.<RolePermissionPO>lambdaQuery()
                        .eq(RolePermissionPO::getRoleId, roleId))
                .stream()
                .map(RolePermissionPO::getPermissionId)
                .toList();

        // 每个permissionId查询到permissionName
        Set<String> permissionNames = new HashSet<>();
        for (Long pId : permissionIds) {
            PermissionPO po = permissionMapper.selectById(pId);
            // 重要！
            if (pId != null) {
                permissionNames.add(po.getName());
            }
        }
        return permissionNames;
    }

    // 检查用户名密码合法性，非法抛出异常
    private void checkValidUsernameAndPassword(String username, String password) {

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
        return getFullInfoByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户 " + username + " 找不到"));
    }
}
