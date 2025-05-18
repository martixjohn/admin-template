package com.example.demo.repository.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.common.pojo.po.RolePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author martix
 * @description
 * @time 5/12/25 9:03 AM
 */
@Mapper
public interface RoleMapper extends BaseMapper<RolePO> {


    /**
     * 查询是否有 某个角色
     *
     * @param roleName 角色名
     * @return 是否有
     */
    default boolean existsRole(String roleName) {
        return exists(Wrappers.<RolePO>lambdaQuery().eq(RolePO::getName, roleName));
    }

    /**
     * 按角色名查询
     *
     * @param roleName 角色名
     * @return 角色
     */
    default RolePO selectByRoleName(String roleName) {
        return selectOne(Wrappers.<RolePO>lambdaQuery().eq(RolePO::getName, roleName));
    }

}
