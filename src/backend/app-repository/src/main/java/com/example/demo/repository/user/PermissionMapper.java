package com.example.demo.repository.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.common.pojo.po.PermissionPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author martix
 * @description
 * @time 5/12/25 9:03 AM
 */
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionPO> {
}
