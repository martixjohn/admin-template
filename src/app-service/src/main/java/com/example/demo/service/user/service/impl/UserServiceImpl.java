package com.example.demo.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.user.dao.*;
import com.example.demo.user.pojo.entity.*;
import com.example.demo.user.pojo.service.User;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author martix
 * @description
 * @time 2025/4/21 22:02
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements UserService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;

    @Override
    public User getFullInfoByUsername(String username) {
        User res = new User();
        UserPO userPO = userMapper
                .selectOne(
                        Wrappers.<UserPO>lambdaQuery()
                                .select(UserPO::getUsername)
                );

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
        return res;
    }


    @Override
    public Set<String> getPermissionsByRole(String role) {
        RolePO rolePO = roleMapper.selectOne(Wrappers.<RolePO>lambdaQuery().eq(RolePO::getName, role));
        if (rolePO == null) {
            return new HashSet<>(0);
        }
        return getPermissionByRoleId(rolePO.getId());
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

}
