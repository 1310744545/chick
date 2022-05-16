package com.chick.download.proxy.controller;

import cn.hutool.core.io.FileUtil;
import com.chick.annotation.LoginUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName jdkDownloadController
 * @Author xiaokexin
 * @Date 2021/12/3 12:54
 * @Description jdkDownloadController
 * @Version 1.0
 */
@Controller
@RequestMapping("/Jdk")
public class JDKController {


    @ResponseBody
    @PostMapping("/test")
    public String test(@LoginUser String asdasd, String qweqwe){
        String picPathOut = System.getProperty("catalina.home");
        if (FileUtil.isWindows()) {
            picPathOut = picPathOut.substring(0, picPathOut.lastIndexOf("\\"));
        } else {
            picPathOut = picPathOut.substring(0, picPathOut.lastIndexOf("/"));
        }
        picPathOut += "/" + "apache-tomcat-7164-cszmEcard" + "/webapps/cszmEcard";
        return picPathOut;
    }

    @ResponseBody
    @PostMapping("/test2")
    public String test2(){
        String picPathOut = System.getProperty("catalina.home");
        if (FileUtil.isWindows()) {
            picPathOut = picPathOut.substring(0, picPathOut.lastIndexOf("\\"));
        } else {
            picPathOut = picPathOut.substring(0, picPathOut.lastIndexOf("/"));
        }
        picPathOut += "/" + "apache-tomcat-7164-cszmEcard" + "/webapps/cszmEcard22222";
        List<Object> objects = new ArrayList<>();

        return picPathOut;
    }
}
