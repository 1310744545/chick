package com.chick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.pojo.entity.Role;

/**
 * <p>
 * 用户角色表  服务类
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27
 */
public interface IRoleService extends IService<Role> {

    R getRole();

    R saveRole(Role menu);

    R updateRole(Role menu);

    R removeRole(Role menu);
}
