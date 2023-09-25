package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author ldd
 */
@SpringBootApplication
@EnableOpenApi
//@EnableAdminServer
@EnableScheduling
@Slf4j
public class DemoApplication {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        SpringApplication.run(DemoApplication.class);
        long end = System.currentTimeMillis();
        log.info("start success "+(end - start)+"ms");
    }
}