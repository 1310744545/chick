package com.chick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chick.pojo.entity.Role;
import com.chick.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27 16:07
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询用户的所有权限
     * @param username
     * @return
     */
    List<String> getPermissionByUserName(String username);

    /**
     * 根据用户ID获取用户角色
     *
     * @param username 用户名
     * @return 用户角色
     */
    List<Role> selectUserRole(String username);

    /**
     * 根据用户ID获取用户名
     *
     * @param userId 用户ID
     * @return 用户角色
     */
    String getNameByUserId(String userId);
}
