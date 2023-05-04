package org.example.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 测试spring-boot-admin中能否监测到定时任务
 * @author ldd
 */
@Component
@Slf4j
public class SpringBootAdminSchedule {
    static int i = 0;
    @Scheduled(cron = "* * * * * ?")
    public void run(){
        log.info("SpringBootAdminSchedule-执行次数："+i++);
    }
}
