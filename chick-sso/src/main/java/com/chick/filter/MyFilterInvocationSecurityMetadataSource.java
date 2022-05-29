package com.chick.filter;

import com.chick.base.CommonConstants;
import com.chick.common.utils.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @ClassName MyFilterInvocationSecurityMetadataSource
 * @Author xiaokexin
 * @Date 2022-05-27 16:07
 * @Description 自定义资源保护过滤器
 * @Version 1.0
 */
@Configuration
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation)o;
        //获取请求的地址
        String requestUrl = fi.getRequestUrl().split("\\?")[0];
        String requestKey = CommonConstants.SYS_ROLE_PATH + ":" + requestUrl;
        //不受保护的资源，直接返回null.
        if (!redisUtil.hasKey(requestKey)){
            return null;
        }
        //获取请求的地址所需的权限
        List<Object> roles = redisUtil.range(requestKey, 0, -1);
        Collection<ConfigAttribute> configAttributes = new ArrayList<>();
        for (Object role : roles){
            configAttributes.add(new SecurityConfig(role.toString()));
        }
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
