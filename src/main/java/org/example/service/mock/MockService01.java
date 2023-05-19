package org.example.service.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 测试在spring中被管理的里类如何mock
 * @author ldd
 */
@Service
public class MockService01 {
    @Autowired
    MockService02 mockService02;
    public void doSomeThing(){
        System.out.println(mockService02.doOther());
    }
    public void doSomeThing1(){
        System.out.println(mockService02.doOther1());
    }
    public void printCurrentTimeMillis(){
        System.out.println(System.currentTimeMillis());
    }
}
