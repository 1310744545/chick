package com.chick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.mapper.RoleMapper;
import com.chick.pojo.entity.Role;
import com.chick.service.IRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色表  服务实现类
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
