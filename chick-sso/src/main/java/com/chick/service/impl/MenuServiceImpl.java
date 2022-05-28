package com.chick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.mapper.MenuMapper;
import com.chick.pojo.dto.MenuAndRole;
import com.chick.pojo.entity.Menu;
import com.chick.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单权限表  服务实现类
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public List<MenuAndRole> loadMenuAndRole() {
        return menuMapper.loadMenuAndRole();
    }
}
