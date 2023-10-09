package com.example.redis.controller;

import com.example.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Redis测试Controller
 */
@RestController
public class RedisController {
    @Autowired
    RedisService redisService;
    @GetMapping("/getRedisValue")
    public void getRedisValue(int threadCount,int perThreadCount){
        redisService.threadGetRedis(threadCount,perThreadCount);
    }
    @GetMapping("/setRedisValue")
    public void setRedisValue(int threadCount,int perThreadCount){
        redisService.threadSetRedis(threadCount,perThreadCount);
    }
}
