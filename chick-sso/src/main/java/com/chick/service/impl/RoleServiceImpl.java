package com.chick.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.common.utils.RedisUtil;
import com.chick.mapper.MenuMapper;
import com.chick.mapper.RoleMapper;
import com.chick.pojo.dto.MenuAndRole;
import com.chick.pojo.entity.Menu;
import com.chick.pojo.entity.Role;
import com.chick.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

import static com.chick.common.utils.ChickUtil.DoId;

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

    @Resource
    private MenuMapper menuMapper;
    @Resource
    private RedisUtil redisUtil;
    @Override
    public R getRole() {
        return R.ok(baseMapper.selectList(Wrappers.<Role>lambdaQuery()
                .eq(Role::getDelFlag, CommonConstants.UN_DELETE_FLAG)));
    }

    @Override
    public R saveRole(Role role) {
        role.setRoleId(DoId());
        if (baseMapper.insert(role) == 1){
            return R.ok("保存成功");
        }
        return R.failed("保存失败");
    }

    @Override
    public R updateRole(Role role) {
        if (baseMapper.updateById(role) == 1){
            return R.ok("保存成功");
        }
        return R.failed("保存失败");
    }

    @Override
    public R removeRole(Role role) {
        role.setDelFlag(CommonConstants.DELETE_FLAG);
        if(baseMapper.updateById(role) > 0){
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
}
