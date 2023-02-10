package com.chick.software.controller;

import com.chick.base.CommonConstants;
import com.chick.base.R;
import com.chick.exam.service.ExamService;
import com.chick.software.service.SoftwareService;
import com.chick.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaokexin
 * @since 2022-03-01
 */
@RestController
@RequestMapping("/software")
public class SoftwareController {

    @Resource
    private SoftwareService softwareService;

    @RequestMapping("/test")
    public R upload() {
        return R.ok(new Date());
    }

    @RequestMapping("/testAdmin")
    public R test2() {
        return R.ok(new Date());
    }

    @RequestMapping("/testManager")
    public R test3() {
        return R.ok("test3");
    }

    @RequestMapping("/testVip")
    public R test4() {
        return R.ok("test4");
    }

    @RequestMapping("/testCommon")
    public R test5() {
        return R.ok("test5");
    }

    /**
    * @Author xkx
    * @Description 获取软件列表
    * @Date 2022-12-26 9:13
    * @Param [type, keyword, current, size]
    * @return com.chick.base.R
    **/
    @GetMapping("/getSoftwareList")
    public R getSoftwareList(String type, String keyword, Integer current, Integer size){
        if (StringUtils.isNotBlank(keyword) && keyword.length() > CommonConstants.MAX_NAME_LENGTH) {
            return R.failed("关键字过长");
        }
        return softwareService.getSoftwareList(PageUtils.validPage(current, size), type, keyword);
    }
}

