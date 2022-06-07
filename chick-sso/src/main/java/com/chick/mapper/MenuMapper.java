package com.chick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chick.pojo.dto.MenuAndRole;
import com.chick.pojo.entity.Menu;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * <p>
 * 菜单权限表  Mapper 接口
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27 16:07
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<MenuAndRole> loadMenuAndRole();

    int deleteByPrimaryKey(String menuId);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(String menuId);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);
}
