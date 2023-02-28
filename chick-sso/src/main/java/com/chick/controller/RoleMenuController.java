package com.chick.controller;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.chick.base.R;
import com.chick.pojo.dto.RoleMenuUpdateDTO;
import com.chick.pojo.entity.RoleMenu;
import com.chick.service.IRoleMenuService;
import com.chick.service.IRoleService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 角色菜单配置表  前端控制器
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/roleMenu")
@Api(tags = "角色菜单关系相关接口")
public class RoleMenuController extends BaseController {

    @Resource
    private IRoleService roleService;

    @Resource
    private IRoleMenuService roleMenuService;

    /**
     * @Author xkx
     * @Description 获取全部角色
     * @Date 2022-06-06 17:39
     * @Param []
     * @return com.chick.base.R
     **/
    @GetMapping("/getRoleMenu")
    public R getRole(){
        return roleService.getRole();
    }

    /**
     * @Author xkx
     * @Description 获取全部角色
     * @Date 2022-06-06 17:39
     * @Param []
     * @return com.chick.base.R
     **/
    @GetMapping("/getRoleMenuByRoleId")
    public R getRoleMenuByRoleId(RoleMenu roleMenu){
        return roleMenuService.getRoleMenuByRoleId(roleMenu);
    }

    /**
     * @Author xkx
     * @Description 授权
     * @Date 2022-06-06 17:39
     * @Param []
     * @return com.chick.base.R
     **/
    @PostMapping("/updateRoleMenu")
    public R updateRoleMenu(@RequestBody RoleMenuUpdateDTO roleMenuUpdateDTO){
        return roleMenuService.updateRoleMenu(roleMenuUpdateDTO);
    }
}
