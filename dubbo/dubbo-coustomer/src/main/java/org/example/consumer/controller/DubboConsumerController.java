package org.example.consumer.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.example.service.DubboUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DubboConsumerController {
    @Reference
    DubboUserService dubboUserService;

    @GetMapping("/getUser")
    public String consumerFun() {
        String user = dubboUserService.getUser("user1");
        return user;
    }
}
