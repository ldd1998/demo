package org.example.service.kafka;

import org.example.DemoApplicationForTest;
import org.example.entity.User;
import org.example.util.EntityRandomizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = DemoApplicationForTest.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class KafkaProducerServiceTest {

    @Autowired
    KafkaProducerService kafkaProducerService;
    /**
     * kafka插入速度测试
     * 单线程10W/s，网络达到瓶颈
     */
    @Test
    public void sendMessageOneTest() throws IllegalAccessException, InstantiationException {
        for (int i = 0; i < 1000000; i++) {
            kafkaProducerService.sendMessage("insertTest", EntityRandomizer.getRandomizedEntity(User.class).toString());
        }
    }
}