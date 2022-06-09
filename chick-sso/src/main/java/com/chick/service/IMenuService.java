package com.chick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.base.R;
import com.chick.pojo.dto.MenuAndRole;
import com.chick.pojo.entity.Menu;
import com.chick.web.dictionary.entity.SysDbInfo;

import java.util.List;

/**
 * <p>
 * 菜单权限表  服务类
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27
 */
public interface IMenuService extends IService<Menu> {


    List<MenuAndRole> loadMenuAndRole();

    R getBackStageMenu();

    R saveCustom(Menu menu);

    R update(Menu menu);

    R removeByDataNum(Menu menu);

    R getResource();

    R saveResource(Menu menu);

    R updateResource(Menu menu);

    R removeResource(Menu menu);
}
