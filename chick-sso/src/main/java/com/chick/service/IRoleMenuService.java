package com.chick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.pojo.dto.RoleMenuUpdateDTO;
import com.chick.pojo.entity.RoleMenu;

/**
 * <p>
 * 角色菜单配置表  服务类
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27
 */
public interface IRoleMenuService extends IService<RoleMenu> {

    R getRoleMenuByRoleId(RoleMenu roleMenu);

    R updateRoleMenu(RoleMenuUpdateDTO roleMenuUpdateDTO);
}
