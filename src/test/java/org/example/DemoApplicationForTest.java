package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ldd
 * 只应用于测试启动项目，关闭了不必要的注解
 */
@SpringBootApplication
//@EnableOpenApi
//@EnableAdminServer
//@EnableScheduling
public class DemoApplicationForTest {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplicationForTest.class);
    }
}