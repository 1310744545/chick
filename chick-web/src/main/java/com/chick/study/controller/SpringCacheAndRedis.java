package com.chick.study.controller;

import com.chick.study.service.ISpringCacheAndRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SpringCacheAndRedis
 * @Author xiaokexin
 * @Date 2022-05-17 13:25
 * @Description SpringCacheAndRedis
 * @Version 1.0
 */
@RestController
@RequestMapping("/cache")
public class SpringCacheAndRedis {

    @Autowired
    private ISpringCacheAndRedis iSpringCacheAndRedis;

    @GetMapping("/test")
    public String testRedisCache(String param){
        return iSpringCacheAndRedis.testRedisCache(param);
    }
}
