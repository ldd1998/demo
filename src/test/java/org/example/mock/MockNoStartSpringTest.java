package org.example.mock;

import org.example.service.mock.MockService01;
import org.example.service.mock.MockService02;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * 在带有@Mock和@InjectMocks批注的通常模拟中，被测试的类应与@RunWith(MockitoJUnitRunner.class)运行
 * 可以实现在不启动Spring的情况下进行测试和mock，需要@RunWith(MockitoJUnitRunner.class)
 */
@RunWith(MockitoJUnitRunner.class)
public class MockNoStartSpringTest {
    @InjectMocks
    MockService01 mockService01; // 把下方@Mock注解修饰的对象注入到mockService01中。
    @Mock
    MockService02 mockService02;
    @Test
    public void test01(){
        Mockito.when(mockService02.doOther()).thenReturn("mock success");
        mockService01.doSomeThing();
    }
}
