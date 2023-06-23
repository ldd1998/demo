package org.example.service.tdeninge;

import org.example.DemoApplicationForTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DemoApplicationForTest.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TDeningeServiceTest {
    @Autowired
    TDeningeService tDeningeService;
    @Test
    public void Test(){
        tDeningeService.stbMapperSelectList();
    }
    @Test
    public void Test1(){
        tDeningeService.stbMapperInsert(100000);
    }
    @Test
    public void Test2(){
        tDeningeService.stbMapperSelect();
    }
}
