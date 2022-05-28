package com.chick.software.controller;

import com.chick.base.R;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


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
}

