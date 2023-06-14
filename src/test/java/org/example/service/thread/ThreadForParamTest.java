package org.example.service.thread;

import org.junit.Test;

public class ThreadForParamTest {
    ThreadForParam threadForParam = new ThreadForParam();
    @Test
    public void test01(){
        threadForParam.runThread(100000);
    }
    @Test
    public void test02(){
        threadForParam.runThreadPool(100000);
    }
}