package com.example.demo.repository.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.common.pojo.po.RolePermissionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * 角色_权限
 * @author martix
 * @description
 * @time 5/12/25 9:06 AM
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermissionPO> {
    /**
     * 根据角色查询权限
     *
     * @param roleName 角色名
     * @return 权限
     */
    @Select("""
            SELECT permission.name
            FROM permission,
                 role_permission,
                 role
            WHERE permission.id = role_permission.permission_id
              AND role_permission.role_id = role.id
              AND role.name = #{roleName}
              AND permission.deleted <> FALSE
              AND role_permission.deleted <> FALSE
              AND role.deleted <> FALSE
            """)
    Set<String> selectPermissionsByRoleName(String roleName);
}
