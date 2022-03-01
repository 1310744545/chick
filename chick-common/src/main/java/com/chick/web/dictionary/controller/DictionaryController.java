package com.chick.web.dictionary.controller;

import com.chick.base.R;
import com.chick.web.dictionary.entity.SysDbInfo;
import com.chick.web.dictionary.service.ISysDbInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName DictionaryController
 * @Author xiaokexin
 * @Date 2022/2/16 14:33
 * @Description DictionaryController
 * @Version 1.0
 */
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Autowired
    private ISysDbInfoService sysDbInfoService;

    /**
    * @Author xiaokexin
    * @Description 获取全部字典
    * @Date 2022/2/24 17:35
    * @Param []
    * @return com.chick.base.R
    **/
    @GetMapping("/getAllDbInfo")
    public R getAllDbInfo(){
        return sysDbInfoService.getAllDbInfo();

    }

    /**
    * @Author xiaokexin
    * @Description 保存字典
    * @Date 2022/2/24 17:35
    * @Param [sysDbInfo]
    * @return com.chick.base.R
    **/
    @PostMapping("/save")
    public R save(@RequestBody SysDbInfo sysDbInfo){
        if(ObjectUtils.isEmpty(sysDbInfo)){
            return R.failed("参数错误");
        }
        return sysDbInfoService.save(sysDbInfo);
    }

    /**
     * @Author xiaokexin
     * @Description 更新字典
     * @Date 2022/2/24 17:35
     * @Param [sysDbInfo]
     * @return com.chick.base.R
     **/
    @PostMapping("/update")
    public R update(@RequestBody SysDbInfo sysDbInfo){
        if(ObjectUtils.isEmpty(sysDbInfo)){
            return R.failed("参数错误");
        }
        return sysDbInfoService.update(sysDbInfo);
    }

    /**
    * @Author xiaokexin
    * @Description 根据dataNum作废字典及子字典
    * @Date 2022/2/25 9:35
    * @Param [sysDbInfo]
    * @return com.chick.base.R
    **/
    @PostMapping("/removeByDataNum")
    public R removeByDataNum(@RequestBody SysDbInfo sysDbInfo){
        if(ObjectUtils.isEmpty(sysDbInfo)){
            return R.failed("参数错误");
        }
        return sysDbInfoService.removeByDataNum(sysDbInfo);
    }
}
