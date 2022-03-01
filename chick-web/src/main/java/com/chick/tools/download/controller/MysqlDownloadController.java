package com.chick.tools.download.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName mysqlDownloadController
 * @Author xiaokexin
 * @Date 2021/12/3 12:54
 * @Description mysqlDownloadController
 * @Version 1.0
 */
@RestController
@RequestMapping("/MysqlDownload")
public class MysqlDownloadController {

    @GetMapping("/hello")
    public String hello(){
        return "helloWorld";
    }
}
