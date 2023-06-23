package org.example.service;

import org.example.DemoApplicationForTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DemoApplicationForTest.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CommonServiceTest {
    @Autowired
    CommonService commonService;
    @Test
    public void test(){
        commonService.selectUserByPage(1,1);
    }
}
