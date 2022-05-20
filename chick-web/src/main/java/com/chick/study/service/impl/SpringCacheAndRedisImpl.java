package com.chick.study.service.impl;

import com.chick.study.service.ISpringCacheAndRedis;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @ClassName SpringCacheAndRedisImpl
 * @Author xiaokexin
 * @Date 2022-05-17 13:27
 * @Description SpringCacheAndRedisImpl
 * @Version 1.0
 */
@Service
@CacheConfig(cacheNames = "test:zxc:")
public class SpringCacheAndRedisImpl implements ISpringCacheAndRedis {

    @Override
    @Cacheable(key="methodName+'.'+#param")
    public String testRedisCache(String param) {
        return "qweqweqweqwe";
    }
}
