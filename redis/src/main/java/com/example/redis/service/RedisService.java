package com.example.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

import static com.example.redis.config.ThreadExecutePool.threadPoolExecutor;

/**
 * redis速度测试
 */
@Service
@Slf4j
public class RedisService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    String key = "testKey";
    /**
     * 多线程设置Redis的值
     */
    public void threadSetRedis(int threadCount, int perThreadCount){
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < perThreadCount; i++) {
                        stringRedisTemplate.opsForValue().set(key,"testValue");
                    }
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 多线程获取Redis的值
     */
    public void threadGetRedis(int threadCount, int perThreadCount){
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < perThreadCount; i++) {
                        stringRedisTemplate.opsForValue().get(key);
                    }
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
