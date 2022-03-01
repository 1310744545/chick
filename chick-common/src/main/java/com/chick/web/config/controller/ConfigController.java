package com.chick.web.config.controller;

import com.chick.base.R;
import com.chick.web.config.entity.SysConfig;
import com.chick.web.config.service.ISysConfigService;
import com.chick.web.dictionary.entity.SysDbInfo;
import com.chick.web.dictionary.service.ISysDbInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName ConfigController
 * @Author xiaokexin
 * @Date 2022/2/16 14:33
 * @Description ConfigController
 * @Version 1.0
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ISysConfigService sysConfigService;

    /**
    * @Author xiaokexin
    * @Description 获取全部字典
    * @Date 2022/2/24 17:35
    * @Param []
    * @return com.chick.base.R
    **/
    @GetMapping("/getAllConfigInfo")
    public R getAllDbInfo(){
        return sysConfigService.getAllConfigInfo();

    }

    /**
    * @Author xiaokexin
    * @Description 保存配置，如果有id则更新
    * @Date 2022/2/24 17:35
    * @Param [sysDbInfo]
    * @return com.chick.base.R
    **/
    @PostMapping("/save")
    public R save(@RequestBody SysConfig sysConfig){
        if(ObjectUtils.isEmpty(sysConfig)){
            return R.failed("参数错误");
        }
        return sysConfigService.save(sysConfig);
    }

    /**
     * @Author xiaokexin
     * @Description 更新配置
     * @Date 2022/2/24 17:35
     * @Param [sysDbInfo]
     * @return com.chick.base.R
     **/
    @PostMapping("/update")
    public R update(@RequestBody SysConfig sysConfig){
        if(ObjectUtils.isEmpty(sysConfig)){
            return R.failed("参数错误");
        }
        return sysConfigService.update(sysConfig);
    }

    /**
    * @Author xiaokexin
    * @Description 根据id删除配置
    * @Date 2022/2/25 10:51
    * @Param [sysDbInfo]
    * @return com.chick.base.R
    **/
    @PostMapping("/remove")
    public R remove(@RequestBody SysConfig sysConfig){
        if(ObjectUtils.isEmpty(sysConfig)){
            return R.failed("参数错误");
        }
        return sysConfigService.remove(sysConfig);
    }
}
