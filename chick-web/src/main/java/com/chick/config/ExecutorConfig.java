package com.chick.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

/**
 * @ClassName ExecutorConfig
 * @Author xiaokexin
 * @Date 2022-11-09 11:00
 * @Description ExecuterConfig
 * @Version 1.0
 */
public class ExecutorConfig {
    static {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("async-");
        return executor;
    }

    @Bean
    public DelegatingSecurityContextAsyncTaskExecutor taskExecutor(ThreadPoolTaskExecutor delegate) {
        return new DelegatingSecurityContextAsyncTaskExecutor(delegate);
    }
}
