package org.example.service.task.springBootAdmin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 测试spring-boot-admin中能否监测到定时任务
 * @author ldd
 */
@Component
@Slf4j
public class SpringBootAdminTask {
    static int i = 0;
//    @Scheduled(cron = "0/10 * * * * ?")
    public void run(){
        try {
            log.info("SpringBootAdminSchedule-执行开始，休眠：12秒");
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("SpringBootAdminSchedule-执行结束，次数："+i++);
    }
}
