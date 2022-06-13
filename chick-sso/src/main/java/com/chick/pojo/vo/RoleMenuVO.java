package com.chick.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chick.common.domin.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27
 */
@Data
public class RoleMenuVO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单id
     */
    private String menuId;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单权限标识
     */
    private String permission;

    /**
     * 前端路由标识路径
     */
    private String path;

    /**
     * 菜单类型
     */
    private String type;

    /**
     * 是否禁用
     */
    private boolean disabled;

    /**
     * 前端路由标识路径
     */
    private String parentMenuId;

    /**
     * 该角色是否有当前资源权限
     */
    private String hasThisMenu;


    @TableField(exist = false)
    private List<RoleMenuVO> children;
}
