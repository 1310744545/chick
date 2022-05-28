package com.chick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.mapper.UserRoleMapper;
import com.chick.pojo.entity.UserRole;
import com.chick.service.IUserRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色配置表  服务实现类
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
