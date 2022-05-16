package com.chick.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * 登录参数解析设置类
 *
 * @author green
 * @date 2020/1/6
 * @company 安人股份
 * @desc
 */
@Slf4j
@Configuration
public class DefaultWebMvcConfig extends WebMvcConfigurationSupport {

//    @Lazy
//    @Resource
//    private SsoUserFeign ssoUserFeign;

    /**
     * 令牌参数设置
     *
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        //注入用户信息
        argumentResolvers.add(new TokenArgsResolver());
        if (log.isInfoEnabled()) {
            log.info("已加入登录用户对象参数解析器");
        }
    }

    /**
     * 设置资源文件目录
     *
     * @param registry
     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // 默认静态资源支持
//        registry.addResourceHandler("/**")
//                .addResourceLocations("classpath:/resources/")
//                .addResourceLocations("classpath:/static/")
//                .addResourceLocations("classpath:/public/");
//        // swagger-ui支持
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }

}
