package com.chick.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.base.R;
import com.chick.config.ChickRunner;
import com.chick.mapper.MenuMapper;
import com.chick.pojo.dto.MenuAndRole;
import com.chick.pojo.entity.Menu;
import com.chick.service.IMenuService;
import com.chick.web.dictionary.entity.SysDbInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.chick.common.utils.ChickUtil.DoId;

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

    @Override
    public R getBackStageMenu() {
        List<Menu> menus = menuMapper.selectList(Wrappers.<Menu>lambdaQuery()
                .eq(Menu::getType, "1")
                .eq(Menu::getDelFlag, "0"));
        return R.ok(buildTree(menus, null));
    }

    @Override
    public R saveCustom(Menu menu) {
        menu.setMenuId(DoId());
        if (menuMapper.insert(menu) == 1){
            return R.ok("保存成功");
        }
        return R.failed("保存失败");
    }

    @Override
    public R update(Menu menu) {
        if (menuMapper.updateByPrimaryKeySelective(menu) == 1){
            ChickRunner.loadDictionary();
            return R.ok("更新成功");
        }
        return R.failed("更新失败");
    }

    @Override
    public R removeByDataNum(Menu menu) {
//        if(menuMapper.removeByDataNum(sysDbInfo) > 0){
//            ChickRunner.loadDictionary();
//            return R.ok("删除成功");
//        }
        return R.failed("删除失败");
    }

    private List<Menu> buildTree(List<Menu> list, String pid){
        List<Menu> tree = new ArrayList<>();
        for(Menu menu : list){
            if(Objects.equals(menu.getParentMenuId(), pid)){
                tree.add(findChild(menu, list));
            }
        }
        return tree;
    }
    /**
     * 寻找子节点
     *
     * @param menu
     * @param list
     */
    private Menu findChild(Menu menu, List<Menu> list){
        for(Menu n : list){
            if(Objects.equals(n.getParentMenuId(), menu.getMenuId())){
                if(menu.getChildren() == null){
                    menu.setChildren(new ArrayList<>());
                }
                menu.getChildren().add(findChild(n, list));
            }
        }
        return menu;
    }
}
