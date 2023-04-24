package org.example.mock;

import org.example.service.mock.MockService01;
import org.example.service.mock.MockService02;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

/**
 * 测试如果mock bean对象并让spring管理起来
 */
@SpringBootTest
public class MockTest {
    @Autowired
    MockService01 mockService01;
    @SpyBean
    MockService02 mockService02SpyBean;
    @MockBean
    MockService02 mockService02MockBean;

    /**
     * 测试被@MockBean 注入的对象，Mockito.whenThenReturn 只有被mock的方法才会正常返回，其余方法返回null或0
     */
    @Test
    public void mockTest(){
        Mockito.when(mockService02MockBean.doOther()).thenReturn("mock success");
        mockService01.doSomeThing();// 输出mock success，证明mock成功
        mockService01.doSomeThing1(); // 输出null 证明没有被mock的方法不会按照本来的功能进行执行，而是输出null
    }

    /**
     * 测试被@MockBean 注入的对象，thenCallRealMethod()之后执行自己的真实的逻辑
     */
    @Test
    public void mockThenCallRealMethodTest(){
        Mockito.when(mockService02MockBean.doOther()).thenReturn("mock success");
        Mockito.when(mockService02MockBean.doOther1()).thenCallRealMethod();// 正常执行其他方法
        mockService01.doSomeThing();// 输出mock success，证明mock成功
        mockService01.doSomeThing1(); // 输出MockService02-doOther1 证明执行了自己真实的方法
    }
    /**
     * 测试被@SpyBean注入的对象，Mockito.whenThenReturn之后，其余方法正常执行自己的逻辑
     */
    @Test
    public void mockSpyBeanTest(){
        Mockito.when(mockService02SpyBean.doOther()).thenReturn("mock success");
        mockService01.doSomeThing();// 输出mock success，证明mock成功
        mockService01.doSomeThing1(); // 输出MockService02-doOther1，而不是null 其余方法正常执行自己真实的逻辑
    }

    /**
     * 测试利用doReturnWhen 代替 whenThenReturn，解决类型转换警告
     */
    @Test
    public void mockDoReturnWhenTest(){
        Mockito.doReturn("mock success").when(mockService02SpyBean).doOther();
        mockService01.doSomeThing();// 输出mock success，证明mock成功
    }
}
