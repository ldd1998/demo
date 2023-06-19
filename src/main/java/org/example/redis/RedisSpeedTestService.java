package org.example.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author ldd
 */
@Service
public class RedisSpeedTestService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *  测试redis的插入性能
     * @param count
     * @param threadCount
     */
    public void redisInsert(int count,int threadCount){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadCount,threadCount,1000L, TimeUnit.SECONDS,new LinkedBlockingDeque<>());
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int j = 0; j < threadCount; j++) {
            int jj = j;
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = count * jj; i < count * (jj + 1); i++) {
                        String key = "test:" + i;
                        redisTemplate.opsForValue().set(key,i+"");

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
     * 测试redis的获取性能
     * @param count
     * @param threadCount
     */
    public void redisGetForValue(int count,int threadCount){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadCount,threadCount,1000L, TimeUnit.SECONDS,new LinkedBlockingDeque<>());
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int j = 0; j < threadCount; j++) {
            int jj = j;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = count * jj; i < count * (jj + 1); i++) {
                        String key = "test:" + i;
                        String s = String.valueOf(redisTemplate.boundValueOps(key).get());
                        System.out.println(s);
                    }
                    countDownLatch.countDown();
                }
            });
            threadPoolExecutor.execute(thread);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
