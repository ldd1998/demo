package org.example.service.kafka;

import org.example.DemoApplicationForTest;
import org.example.entity.User;
import org.example.util.EntityRandomizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@SpringBootTest(classes = DemoApplicationForTest.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class KafkaProducerServiceTest {

    @Autowired
    KafkaProducerService kafkaProducerService;
    String topic = "insertTest";
    /**
     * kafka插入速度测试
     * 单线程10W/s，网络达到瓶颈
     * 在部署到同一台服务器上之后，同样也是10W/s，说明单线程已经最快
     */
    @Test
    public void sendMessageOneTest() {
        for (int i = 0; i < 1000000; i++) {
            kafkaProducerService.sendMessage(topic, EntityRandomizer.getRandomizedEntity(User.class).toString());
        }
    }

    /**
     * 多线程下
     */
    @Test
    public void sendMessageThreadTest() throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20,20,1, TimeUnit.HOURS,new LinkedBlockingDeque<>());
        int threadCount = 1000;
        int perCount = 100000;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < perCount; j++) {
                        kafkaProducerService.sendMessage(topic,EntityRandomizer.getRandomizedEntity(User.class).toString());
                    }
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
    }
}