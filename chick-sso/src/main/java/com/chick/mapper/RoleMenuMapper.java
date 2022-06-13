package com.chick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chick.pojo.entity.RoleMenu;
import com.chick.pojo.vo.RoleMenuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 角色菜单配置表  Mapper 接口
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27 16:07
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    List<RoleMenuVO> getRoleMenuByRoleId(String roleId);
}
