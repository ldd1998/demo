package org.example.elasticsearch;


import lombok.extern.slf4j.Slf4j;
import org.example.DemoApplicationForTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest(classes = DemoApplicationForTest.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
public class ElasticsearchServiceTest {
    @Autowired
    ElasticsearchService elasticsearchService;

    /**
     * 只有30天有效期，暂时不破解了
     * @throws IOException
     */
    @Test
    public void selectListEs() throws IOException {
        elasticsearchService.selectListEs();
    }
}