package com.example.demo.repository.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.common.pojo.po.UserPO;
import com.example.demo.common.pojo.dto.QueryCondition;
import com.example.demo.common.pojo.dto.User;
import jakarta.annotation.Nullable;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户
 *
 * @author martix
 * @description
 * @time 2025/5/12
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPO> {

    /**
     * 查询出包含角色权限信息的所有信息
     *
     * @param username 用户名
     * @return 完整用户信息
     */
    User selectOneByUsername(String username);


    default UserPO selectOnePOByUsername(String username) {
        return selectOne(Wrappers.<UserPO>lambdaQuery().eq(UserPO::getUsername, username));
    }




    /**
     * 批量查询用户
     *
     * @param page            分页查询
     * @param queryConditions 查询条件
     * @return 用户
     */
    IPage<UserPO> selectPage(IPage<UserPO> page, @Param("query") @Nullable List<QueryCondition> queryConditions);

    /**
     * 修改用户，按用户名
     *
     * @param userPO 用户，会完全覆盖
     * @description 应当先查询得到实体类，更改后调用该函数
     */
    default void updateByUsername(UserPO userPO) {
        update(userPO, Wrappers.<UserPO>lambdaQuery().eq(UserPO::getUsername, userPO.getUsername()));
    }

    void updateLastLoginTime(String username);

    /**
     * 删除用户
     * @param username 用户名
     * @return 删除成功
     */
    default boolean deleteByUsername(String username) {
        return delete(Wrappers.<UserPO>lambdaQuery().eq(UserPO::getUsername, username)) == 1;
    }
}
