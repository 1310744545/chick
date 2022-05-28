package com.chick.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chick.mapper.MenuMapper;
import com.chick.pojo.dto.MenuAndRole;
import com.chick.pojo.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;

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
}
