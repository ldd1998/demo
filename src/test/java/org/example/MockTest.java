package org.example;

import org.example.service.MockService01;
import org.example.service.MockService02;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class MockTest {
    @Autowired
    MockService01 mockService01;
    @MockBean
    MockService02 mockService02;
    @Test
    public void mockTest(){
        Mockito.when(mockService02.doOther()).thenReturn("mock");
        mockService01.doSomeThing();
        mockService01.doSomeThing1();
//        mockService02.doOther();
    }
}
