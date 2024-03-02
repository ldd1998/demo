package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
public class DubboProvider {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        SpringApplication.run(DubboProvider.class);
        long end = System.currentTimeMillis();
//        log.info("start success {}ms", end - start);
    }
}