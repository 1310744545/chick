package com.chick.software.controller;


import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.software.service.SoftwareDetailService;
import com.chick.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaokexin
 * @since 2022-03-03
 */
@RestController
@RequestMapping("/softwareDetail")
public class SoftwareDetailController {

    @Resource
    private SoftwareDetailService softwareDetailService;

    /**
     * @Author xkx
     * @Description 获取软件详情列表
     * @Date 2022-12-26 9:13
     * @Param [type, keyword, current, size]
     * @return com.chick.base.R
     **/
    @GetMapping("/getSoftwareDetailList")
    public R getSoftwareDetailList(String type, String keyword, Integer current, Integer size){
        if (StringUtils.isNotBlank(keyword) && keyword.length() > CommonConstants.MAX_NAME_LENGTH) {
            return R.failed("关键字过长");
        }
        return softwareDetailService.getSoftwareDetailList(PageUtils.validPage(current, size), type, keyword);
    }
}

