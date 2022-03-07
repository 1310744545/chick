package com.chick.download.proxy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName mavenDownloadController
 * @Author xiaokexin
 * @Date 2021/12/3 12:54
 * @Description mavenDownloadController
 * @Version 1.0
 */
@RestController
@RequestMapping("/maven")
public class MavenController {

    @GetMapping("/hello")
    public String hello(){
        return "helloWorld";
    }
}