package com.chick.controller;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.chick.base.R;
import com.chick.pojo.entity.Menu;
import com.chick.pojo.entity.Role;
import com.chick.service.IRoleService;
import com.chick.service.impl.RoleServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

import static com.chick.common.utils.ChickUtil.DoId;

/**
 * <p>
 * 用户角色表  前端控制器
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27 16:07
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Resource
    private IRoleService roleService;

    /**
     * @Author xkx
     * @Description 获取全部角色
     * @Date 2022-06-06 17:39
     * @Param []
     * @return com.chick.base.R
     **/
    @GetMapping("/getRole")
    public R getRole(){
        return roleService.getRole();
    }

    /**
     * @Author xiaokexin
     * @Description 保存角色
     * @Date 2022/2/24 17:35
     * @Param [sysDbInfo]
     * @return com.chick.base.R
     **/
    @PostMapping("/saveRole")
    public R saveRole(@RequestBody Role role){
        if(ObjectUtils.isEmpty(role)){
            return R.failed("参数错误");
        }
        return roleService.saveRole(role);
    }

    /**
     * @Author xiaokexin
     * @Description 更新角色
     * @Date 2022/2/24 17:35
     * @Param [sysDbInfo]
     * @return com.chick.base.R
     **/
    @PostMapping("/updateRole")
    public R updateRole(@RequestBody Role role){
        if(ObjectUtils.isEmpty(role)){
            return R.failed("参数错误");
        }
        return roleService.updateRole(role);
    }

    /**
     * @Author xiaokexin
     * @Description 删除角色
     * @Date 2022/2/25 9:35
     * @Param [sysDbInfo]
     * @return com.chick.base.R
     **/
    @PostMapping("/removeRole")
    public R removeRole(@RequestBody Role role){
        if(ObjectUtils.isEmpty(role)){
            return R.failed("参数错误");
        }
        return roleService.removeRole(role);
    }
}
