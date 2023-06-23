package org.example.service.tdeninge;

import org.example.DemoApplicationForTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DemoApplicationForTest.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TDengineTest {
    @Autowired
    TDeningeService tDeningeService;
    @Test
    public void Test(){
        tDeningeService.stbMapperSelectList();
    }
}
