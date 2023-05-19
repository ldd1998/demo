package org.example.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

@RunWith(PowerMockRunner.class)
@PrepareForTest(System.class)
public class MockSystemTimeTest {
    @Test
    public void testSomething() throws Exception {
        long mockCurrentTimeMillis = 1620637200000L; // 设置模拟的时间戳

        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.currentTimeMillis()).thenReturn(mockCurrentTimeMillis);

        System.out.println(System.currentTimeMillis()); // 这里被mock成功，
        System.out.println(new Date().getTime()); // 这里没被mock
    }
}
