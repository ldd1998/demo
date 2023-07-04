package org.example.service.MybatisSelect;

import org.example.DemoApplicationForTest;
import org.example.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DemoApplicationForTest.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MybatisSelectTest {

    @Autowired
    MybatisPlusSelectService mybatisPlusSelectService;
    @Test
    public void test01(){
        User user = mybatisPlusSelectService.selectOne();
        System.out.println(user);
    }

}