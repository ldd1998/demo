package org.example.redis;

import org.example.config.RedisTestConfig;
import org.example.service.redis.RedisSpeedTestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisTestConfig.class)
@Import(RedisSpeedTestService.class)
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
        redisSpeedTestService.redisInsert(10000,1);
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