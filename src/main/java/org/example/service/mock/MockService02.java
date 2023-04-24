package org.example.service.mock;

import org.springframework.stereotype.Service;

/**
 * Mock测试
 * @author ldd
 */
@Service
public class MockService02 {
    public String doOther(){
        return "MockService02-doOther";
    }
    public String doOther1(){
        return "MockService02-doOther1";
    }
}
