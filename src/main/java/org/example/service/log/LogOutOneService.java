package org.example.service.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author ldd
 * 日志记录到单独的一个one文件
 */
@Slf4j
@Component
public class LogOutOneService {
    static int i = 0;
    @Scheduled(fixedDelay = 1000L)
    void printLogTest(){
        log.info("print log one " + i++);
    }
}
