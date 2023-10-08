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
     * 测试redis的插入速度，100个线程每个10000数据，10W/s
     * 但是当前机器和redis所在机器资源占用并不是很多，会不会是因为redis单线程的原因
     * 感觉也不是很快的样子
     */
    @Test
    public void test01(){
        redisSpeedTestService.redisInsert(10000,100);
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