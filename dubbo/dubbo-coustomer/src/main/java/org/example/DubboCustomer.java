package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class DubboCustomer {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        SpringApplication.run(DubboCustomer.class);
        long end = System.currentTimeMillis();
        log.info("start success "+(end - start)+"ms");
    }
}