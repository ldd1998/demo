package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Mock测试
 * @author ldd
 */
@Service
public class MockService02 {
    public String doOther(){
        return "other";
    }
    public String doOther1(){
        return "other1";
    }
}
