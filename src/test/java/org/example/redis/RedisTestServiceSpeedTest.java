package org.example.redis;

import org.example.DemoApplicationForTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest(classes = DemoApplicationForTest.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RedisTestServiceSpeedTest {
    @Autowired
    RedisSpeedTestService redisSpeedTestService;

    /**
     * 测试redis的插入速度
     * 约50000/s
     * 感觉也不是很快的样子
     */
    @Test
    public void test01(){
        redisSpeedTestService.redisInsert(100,20);
    }

    /**
     * 读取20W需要4秒
     * 50000/s
     * 感觉也不是很快
     */

    @Test
    public void redisGetForValue() {
        redisSpeedTestService.redisGetForValue(10000,20);
    }
}