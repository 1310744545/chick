package com.chick.download.springcloud.controller;

import com.chick.base.R;
import com.chick.download.proxy.service.INginxService;
import com.chick.download.springcloud.service.INacosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName NacosController
 * @Author xiaokexin
 * @Date 2022/3/7 16:29
 * @Description NacosController
 * @Version 1.0
 */

@RestController
@RequestMapping("/nacos")
public class NacosController {

    @Autowired
    private INacosService nacosService;

    /**
     * @return java.lang.String
     * @Author xiaokexin
     * @Description 查看是否有新版本，并下载
     * @Date 2022/3/1 15:05
     * @Param []
     **/
    @GetMapping("/download")
    public R download() {
        return nacosService.download();
    }
}
