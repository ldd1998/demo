package org.example.service.thread;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 在for循环中的多线程如何传递i
 * @author ldd
 */
public class ThreadForParam {
    public void runThread(int count){
        for (int i = 0; i < count; i++) {
            int currentValue = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    fun(currentValue);
                }
            };
            ThreadUtil.execute(runnable);
        }
    }
    public void runThreadPool(int count){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,10,100, TimeUnit.HOURS,new LinkedBlockingDeque<>(count));
        for (int i = 0; i < count; i++) {
            int currentValue = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    fun(currentValue);
                }
            };
            threadPoolExecutor.execute(runnable);
        }
    }
    public void fun(int i){
        System.out.println("执行参数："+i);
    }
}
