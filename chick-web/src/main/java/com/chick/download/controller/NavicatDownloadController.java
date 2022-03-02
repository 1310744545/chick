package com.chick.download.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName jdkDownloadController
 * @Author xiaokexin
 * @Date 2021/12/3 12:54
 * @Description jdkDownloadController
 * @Version 1.0
 */
@RestController
@RequestMapping("/NavicatDownload")
public class NavicatDownloadController {

    @GetMapping("/hello")
    public String hello(){
        return "helloWorld";
    }
}
