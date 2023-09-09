package org.example.service.task.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ldd
 * 日志记录到单独的一个one文件
 */
@Slf4j
@Component
public class LogOutOneTask {
    static int i = 0;
//    @Scheduled(fixedDelay = 1000L)
    void printLogTest(){
        log.info("print log one " + i++);
    }
}
