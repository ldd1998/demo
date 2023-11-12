package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @Schedule默认最多8个，这里设置为100
 */
@Configuration
@EnableAsync // 和这个也没有关系
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100); // 设置核心线程数
        executor.setMaxPoolSize(100); // 设置最大线程数
        executor.setQueueCapacity(500); // 设置队列容量
        executor.setThreadNamePrefix("MyAsyncThread-");
        executor.initialize();
        return executor;
    }
}
