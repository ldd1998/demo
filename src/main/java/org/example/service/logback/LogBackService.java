package org.example.service.logback;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ldd
 * 6、不要在千层循环中打印日志
 * 这个是什么意思，如果你的框架使用了性能不高的 Log4j 框架，那就不要在上千个 for循环中打印日志，
 * 这样可能会拖垮你的应用程序，如果你的程序响应时间变慢，那要考虑是不是日志打印的过多了
 * 测试logback性能
 * 1000000条不到0.1秒，在控制台中看是有卡顿的
 * 测试log4j
 * 好像一样，没测出来
 */
@Slf4j
public class LogBackService {
    public static void main(String[] args) {
        for (int i = 0; i < 1000000; i++) {
            log.info("测试日志："+i);
        }
    }
}
