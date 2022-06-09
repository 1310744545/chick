package com.chick.controller;


import com.chick.base.R;
import com.chick.pojo.entity.Menu;
import com.chick.service.IMenuService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 菜单权限表  前端控制器
 * </p>
 *
 * @author 肖可欣
 * @since 2022-05-27 16:07
 */
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {

    @Autowired
    private IMenuService menuService;

    /**
    * @Author xkx
    * @Description 获取全部菜单
    * @Date 2022-06-06 17:39
    * @Param []
    * @return com.chick.base.R
    **/
    @GetMapping("/getBackStageMenu")
    public R getBackStageMenu(){
        return menuService.getBackStageMenu();
    }

    /**
     * @Author xiaokexin
     * @Description 保存菜单
     * @Date 2022/2/24 17:35
     * @Param [sysDbInfo]
     * @return com.chick.base.R
     **/
    @PostMapping("/save")
    public R save(@RequestBody Menu menu){
        if(ObjectUtils.isEmpty(menu)){
            return R.failed("参数错误");
        }
        return menuService.saveCustom(menu);
    }

    /**
     * @Author xiaokexin
     * @Description 更新菜单
     * @Date 2022/2/24 17:35
     * @Param [sysDbInfo]
     * @return com.chick.base.R
     **/
    @PostMapping("/update")
    public R update(@RequestBody Menu menu){
        if(ObjectUtils.isEmpty(menu)){
            return R.failed("参数错误");
        }
        return menuService.update(menu);
    }

    /**
     * @Author xiaokexin
     * @Description 删除菜单
     * @Date 2022/2/25 9:35
     * @Param [sysDbInfo]
     * @return com.chick.base.R
     **/
    @PostMapping("/removeByDataNum")
    public R removeByDataNum(@RequestBody Menu menu){
        if(ObjectUtils.isEmpty(menu)){
            return R.failed("参数错误");
        }
        return menuService.removeByDataNum(menu);
    }


    /**
     * @Author xkx
     * @Description 获取全部资源
     * @Date 2022-06-06 17:39
     * @Param []
     * @return com.chick.base.R
     **/
    @GetMapping("/getResource")
    public R getResource(){
        return menuService.getResource();
    }

    /**
     * @Author xiaokexin
     * @Description 保存资源
     * @Date 2022/2/24 17:35
     * @Param [sysDbInfo]
     * @return com.chick.base.R
     **/
    @PostMapping("/saveResource")
    public R saveResource(@RequestBody Menu menu){
        if(ObjectUtils.isEmpty(menu)){
            return R.failed("参数错误");
        }
        return menuService.saveResource(menu);
    }

    /**
     * @Author xiaokexin
     * @Description 更新资源
     * @Date 2022/2/24 17:35
     * @Param [sysDbInfo]
     * @return com.chick.base.R
     **/
    @PostMapping("/updateResource")
    public R updateResource(@RequestBody Menu menu){
        if(ObjectUtils.isEmpty(menu)){
            return R.failed("参数错误");
        }
        return menuService.updateResource(menu);
    }

    /**
     * @Author xiaokexin
     * @Description 删除资源
     * @Date 2022/2/25 9:35
     * @Param [sysDbInfo]
     * @return com.chick.base.R
     **/
    @PostMapping("/removeResource")
    public R removeResource(@RequestBody Menu menu){
        if(ObjectUtils.isEmpty(menu)){
            return R.failed("参数错误");
        }
        return menuService.removeResource(menu);
    }
}
