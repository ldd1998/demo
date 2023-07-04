package org.example.service;

import org.example.DemoApplicationForTest;
import org.example.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = DemoApplicationForTest.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CommonServiceTest {
    @Autowired
    CommonService commonService;
    @Test
    public void test(){
        List<User> users = commonService.selectUserByPage(1, 1);
        System.out.println(users);
    }
}
