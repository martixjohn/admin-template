package com.example.demo.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.config.AppSecurityProperties;
import com.example.demo.common.exception.AppServiceException;
import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.common.pojo.entity.*;
import com.example.demo.repository.user.*;
import com.example.demo.common.pojo.service.User;
import com.example.demo.service.user.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author martix
 * @description
 * @time 2025/4/21 22:02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements UserService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;
    private final AppSecurityProperties appSecurityProperties;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextHolderStrategy securityContextHolderStrategy;
    // 用户名匹配
    private final Pattern USERNAME_PATTERN;
    // 密码匹配
    private final Pattern PASSWORD_PATTERN;

    public UserServiceImpl(UserMapper userMapper, UserRoleMapper userRoleMapper, RoleMapper roleMapper, RolePermissionMapper rolePermissionMapper, PermissionMapper permissionMapper, AppSecurityProperties appSecurityProperties, PasswordEncoder passwordEncoder, SecurityContextHolderStrategy securityContextHolderStrategy) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
        this.roleMapper = roleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
        this.permissionMapper = permissionMapper;
        this.appSecurityProperties = appSecurityProperties;
        this.passwordEncoder = passwordEncoder;
        this.USERNAME_PATTERN = Pattern.compile(appSecurityProperties.getAccount().getUsernamePattern());
        this.PASSWORD_PATTERN = Pattern.compile(appSecurityProperties.getAccount().getPasswordPattern());
        this.securityContextHolderStrategy = securityContextHolderStrategy;
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
                                .select(UserPO::getUsername)
                );
        if (userPO == null)
            return Optional.empty();

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
        checkUsernameAndPassword(username, password);
        User user = getFullInfoByUsername(username)
                .orElseThrow(() -> new AppServiceException(ExceptionCode.BAD_REQUEST, "用户找不到"));
        String correctPassword = user.getPassword();
        if (!passwordEncoder.matches(password, correctPassword)) {
            throw new AppServiceException(ExceptionCode.BAD_REQUEST, "用户名或密码错误");
        }
        //TODO 登录逻辑，存Session，Set-Cookie等
//        Authentication authentication = UsernamePasswordAuthenticationToken
//                .authenticated(user, correctPassword, );
//        SecurityContext securityContext = securityContextHolderStrategy.createEmptyContext();
//        securityContext.setAuthentication();
    }

    @Override
    public void logout() {
        // TODO 登出逻辑
    }

    @Override
    public void register(String username, String password) {
        checkUsernameAndPassword(username, password);

        boolean present = getFullInfoByUsername(username).isPresent();
        if (present) {
            throw new AppServiceException(ExceptionCode.BAD_REQUEST, "用户已存在");
        }

        String encodedPassword = passwordEncoder.encode(password);
        UserPO userPO = new UserPO();
        userPO.setUsername(username);
        userPO.setPassword(encodedPassword);
        userMapper.insert(userPO);
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
    private void checkUsernameAndPassword(String username, String password, String wrongMsg) {
        if (!validateUsername(username) || !validatePassword(password)) {
            throw new AppServiceException(ExceptionCode.WRONG_AUTHENTICATION, wrongMsg);
        }
    }

    // 检查用户名密码合法性，非法抛出异常
    private void checkUsernameAndPassword(String username, String password) {
        if (!validateUsername(username) || !validatePassword(password)) {
            throw new AppServiceException(ExceptionCode.WRONG_AUTHENTICATION);
        }
    }


}
