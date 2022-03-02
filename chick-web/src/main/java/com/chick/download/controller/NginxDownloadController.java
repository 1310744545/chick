package com.chick.download.controller;

import com.chick.base.R;
import com.chick.download.service.INginxDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName NginxDownloadController
 * @Author xiaokexin
 * @Date 2021/12/3 12:54
 * @Description NginxDownloadController
 * @Version 1.0
 */
@RestController
@RequestMapping("/nginx")
public class NginxDownloadController {

    @Autowired
    private INginxDownloadService nginxDownloadService;

    /**
    * @Author xiaokexin
    * @Description 查看是否有新版本，并下载
    * @Date 2022/3/1 15:05
    * @Param []
    * @return java.lang.String
    **/
    @GetMapping("/download")
    public R download(){
        return nginxDownloadService.download();
    }

}
