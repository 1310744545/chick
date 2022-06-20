package com.chick.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.common.utils.RedisUtil;
import com.chick.config.ChickRunner;
import com.chick.mapper.MenuMapper;
import com.chick.pojo.dto.MenuAndRole;
import com.chick.pojo.entity.Menu;
import com.chick.service.IMenuService;
import com.chick.web.dictionary.entity.SysDbInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<MenuAndRole> loadMenuAndRole() {
        return menuMapper.loadMenuAndRole();
    }

    @Override
    public R getBackStageMenu() {
        List<Menu> menus = menuMapper.selectList(Wrappers.<Menu>lambdaQuery()
                .eq(Menu::getType, CommonConstants.MENU_TYPE_BACKSTAGE)
                .eq(Menu::getDelFlag, CommonConstants.UN_DELETE_FLAG)
                .orderByAsc(Menu::getSort));
        return R.ok(buildTree(menus, null));
    }

    @Override
    public R saveCustom(Menu menu) {
        menu.setMenuId(DoId());
        menu.setType(CommonConstants.MENU_TYPE_BACKSTAGE);
        if (menuMapper.insert(menu) == 1){
            return R.ok("保存成功");
        }
        return R.failed("保存失败");
    }

    @Override
    public R update(Menu menu) {
        if (menuMapper.updateByPrimaryKeySelective(menu) == 1){
            return R.ok("更新成功");
        }
        return R.failed("更新失败");
    }

    @Override
    public R removeByDataNum(Menu menu) {
        List<Menu> menus = menuMapper.selectList(Wrappers.<Menu>lambdaQuery()
                .eq(Menu::getType, CommonConstants.MENU_TYPE_BACKSTAGE)
                .eq(Menu::getDelFlag, CommonConstants.UN_DELETE_FLAG)
                .eq(Menu::getParentMenuId, menu.getMenuId()));
        if (menus.size() > 0){
            return R.failed("当前节点存在子节点，请先删除子节点");
        }
        menu.setDelFlag(CommonConstants.DELETE_FLAG);
        if(menuMapper.updateById(menu) > 0){
            return R.ok("删除成功");
        }
        return R.failed("删除失败");
    }

    @Override
    public R getResource() {
        List<Menu> menus = menuMapper.selectList(Wrappers.<Menu>lambdaQuery()
                .in(Menu::getType, CommonConstants.MENU_TYPE_INTERFACE_TYPE, CommonConstants.MENU_TYPE_INTERFACE)
                .eq(Menu::getDelFlag, CommonConstants.UN_DELETE_FLAG));
        return R.ok(buildTree(menus, null));
    }

    @Override
    public R saveResource(Menu menu) {
        menu.setMenuId(DoId());
        if (menuMapper.insert(menu) == 1){
            updateResource();
            return R.ok("保存成功");
        }
        return R.failed("保存失败");
    }

    @Override
    public R updateResource(Menu menu) {
        if (menuMapper.updateByPrimaryKeySelective(menu) == 1){
            updateResource();
            return R.ok("更新成功");
        }
        return R.failed("更新失败");
    }

    @Override
    public R removeResource(Menu menu) {
        List<Menu> menus = menuMapper.selectList(Wrappers.<Menu>lambdaQuery()
                .in(Menu::getType, CommonConstants.MENU_TYPE_INTERFACE_TYPE, CommonConstants.MENU_TYPE_INTERFACE)
                .eq(Menu::getDelFlag, CommonConstants.UN_DELETE_FLAG)
                .eq(Menu::getParentMenuId, menu.getMenuId()));
        if (menus.size() > 0){
            return R.failed("当前节点存在子节点，请先删除子节点");
        }
        menu.setDelFlag(CommonConstants.DELETE_FLAG);
        if(menuMapper.updateById(menu) > 0){
            updateResource();
            return R.ok("删除成功");
        }
        return R.failed("删除失败");
    }

    private void updateResource(){
        //将所有资源相关的权限放入缓存
        List<MenuAndRole> menuAndRoles = menuMapper.loadMenuAndRole();
        //删除所有的老权限数据
        Set<String> keys = redisUtil.keys("*" + CommonConstants.SYS_ROLE_PATH + "*");
        if (!CollectionUtils.isEmpty(keys)){
            redisUtil.delete(keys);
        }
        //放入所有的新key
        for (MenuAndRole menuAndRole : menuAndRoles){
            if (!"anonymous".equals(menuAndRole.getPermission())){
                String key = CommonConstants.SYS_ROLE_PATH + ":" + menuAndRole.getPath();
                redisUtil.leftPush(key,  menuAndRole.getRole());
            }
        }
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
