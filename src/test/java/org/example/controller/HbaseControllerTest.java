package org.example.controller;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.DemoApplicationForTest;
import org.example.mapper.dto.HBaseDto;
import org.example.service.hbase.HbaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@SpringBootTest(classes = DemoApplicationForTest.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
class HbaseControllerTest {

    @Autowired
    HbaseService hbaseService;

    // 测试批量插入
    @Test
    void createTableTest() {
        try {
            hbaseService.createTable("test01", "cf");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 单线程插入性能，10000条/7s
     * 100个线程100w条数据 24s
     */
    @Test
    void saveOrUpdateTest() {
        long start = System.currentTimeMillis();
        CountDownLatch count = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            ThreadUtil.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        HBaseDto hBaseDto = new HBaseDto();
                        hBaseDto.setTableName("test");
                        hBaseDto.setRow("rowTest");
                        hBaseDto.setColumn("name");
                        hBaseDto.setColumnFamily("cf");
                        hBaseDto.setValue("value");
                        try {
                            hbaseService.saveOrUpdate(hBaseDto);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    count.countDown();
                }
            });
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        log.info("耗时：" + (end - start));
    }
}