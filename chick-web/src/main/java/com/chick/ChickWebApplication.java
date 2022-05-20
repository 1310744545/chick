package com.chick;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName ChickWebApplication
 * @Author xiaokexin
 * @Date 2021/12/24 15:43
 * @Description ChickWebApplication
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@MapperScan(basePackages = "com.chick.**.mapper")
public class ChickWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChickWebApplication.class, args);
    }
}
