package com.example.redis.config;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadExecutePool {
    public static ThreadPoolExecutor threadPoolExecutor;
    static {
        threadPoolExecutor = new ThreadPoolExecutor(20,100,1000L, TimeUnit.SECONDS,new LinkedBlockingDeque<>());
    }
}
