package org.example.service.task.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @EnableAsync,@Async可以保证同一线程不会阻塞，但是最多只有8个线程，多的进行排队
 */
@Slf4j
@Component
@EnableAsync
@EnableScheduling
public class Async1 {
    @Scheduled(cron = "0/1 * * * * ? ")
    @Async
    public void test1() throws InterruptedException {
        log.info("test1执行开始");
        Thread.sleep(10000);
        log.info("test1执行结束");
    }
}
