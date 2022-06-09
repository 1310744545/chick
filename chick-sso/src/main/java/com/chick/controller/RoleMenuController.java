package com.chick.controller;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.chick.base.R;
import com.chick.service.IRoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/sys/roleMenu")
public class RoleMenuController extends BaseController {

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
}
