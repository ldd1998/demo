package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.DemoApplicationForTest;
import org.example.mapper.dto.HBaseDto;
import org.example.service.hbase.HbaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

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
     */
    @Test
    void saveOrUpdateTest() {
        long start = System.currentTimeMillis();
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
        long end = System.currentTimeMillis();
        log.info("耗时：" + (end - start));
    }
}