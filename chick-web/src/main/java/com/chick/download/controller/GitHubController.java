package com.chick.download.controller;

import com.chick.base.R;
import com.chick.download.service.IGitHubService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName GitHubController
 * @Author xiaokexin
 * @Date 2022-05-23 11:21
 * @Description GitHubController
 * @Version 1.0
 */
@RestController
@RequestMapping("/githubDownload")
public class GitHubController {

    @Resource
    private IGitHubService gitHubService;

    @GetMapping("/nacos")
    public R nacos(){
        return gitHubService.nacosDownload();
    }

    @GetMapping("/sentinel")
    public R sentinel(){
        return gitHubService.sentinelDownload();
    }

    @GetMapping("/dubbo")
    public R dubbo(){
        return gitHubService.dubboDownload();
    }

    @GetMapping("/zookeeper")
    public R zookeeper(){
        return gitHubService.zookeeperDownload();
    }
}
