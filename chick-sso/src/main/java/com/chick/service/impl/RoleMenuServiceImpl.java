package com.chick.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.common.utils.RedisUtil;
import com.chick.mapper.MenuMapper;
import com.chick.mapper.RoleMenuMapper;
import com.chick.pojo.dto.MenuAndRole;
import com.chick.pojo.dto.RoleMenuUpdateDTO;
import com.chick.pojo.entity.Menu;
import com.chick.pojo.entity.RoleMenu;
import com.chick.pojo.vo.RoleMenuVO;
import com.chick.service.IRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.management.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * 角色菜单配置表  服务实现类
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private RedisUtil redisUtil;
    @Override
    public R getRoleMenuByRoleId(RoleMenu roleMenu) {
        List<RoleMenuVO> roleMenus = roleMenuMapper.getRoleMenuByRoleId(roleMenu.getRoleId());
        return R.ok(buildTree(roleMenus, null));
    }

    @Override
    public R updateRoleMenu(RoleMenuUpdateDTO roleMenuUpdateDTO) {
        baseMapper.delete(Wrappers.<RoleMenu>lambdaQuery()
                .eq(RoleMenu::getRoleId, roleMenuUpdateDTO.getRoleId()));
        for(String menuId : roleMenuUpdateDTO.getMenuIds()){
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleMenuUpdateDTO.getRoleId());
            roleMenu.setMenuId(menuId);
            baseMapper.insert(roleMenu);
        }
        updateResource();
        return R.ok("授权成功");
    }

    private List<RoleMenuVO> buildTree(List<RoleMenuVO> list, String pid){
        List<RoleMenuVO> tree = new ArrayList<>();
        for(RoleMenuVO menu : list){
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
    private RoleMenuVO findChild(RoleMenuVO menu, List<RoleMenuVO> list){
        for(RoleMenuVO n : list){
            if(Objects.equals(n.getParentMenuId(), menu.getMenuId())){
                if(menu.getChildren() == null){
                    menu.setChildren(new ArrayList<>());
                }
                menu.getChildren().add(findChild(n, list));
            }
        }
        return menu;
    }

    /**
    * @Author xkx
    * @Description 更新权限
    * @Date 2022-06-09 13:27
    * @Param []
    * @return void
    **/
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
}
