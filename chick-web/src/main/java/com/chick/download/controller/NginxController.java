package com.chick.download.controller;

import com.chick.base.R;
import com.chick.download.service.INginxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName NginxDownloadController
 * @Author xiaokexin
 * @Date 2021/12/3 12:54
 * @Description NginxDownloadController
 * @Version 1.0
 */
@RestController
@RequestMapping("/nginx")
public class NginxController {

    @Resource
    private INginxService nginxService;

    /**
     * @return java.lang.String
     * @Author xiaokexin
     * @Description 查看是否有新版本，并下载
     * @Date 2022/3/1 15:05
     * @Param []
     **/
    @GetMapping("/download")
    public R download() {
        return nginxService.download();
    }

}
