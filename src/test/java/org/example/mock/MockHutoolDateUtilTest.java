package org.example.mock;

import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DateUtil.class)
public class MockHutoolDateUtilTest {
    @Test
    public void test01(){
        PowerMockito.mock(DateUtil.class);
        PowerMockito.when(DateUtil.now()).thenReturn("2023");
        System.out.println(DateUtil.now());
        DateUtilService dateUtilService = new DateUtilService();
        dateUtilService.getDateTime();
    }
}
class DateUtilService{
    public void getDateTime(){
        String now = DateUtil.now();
        System.out.println(now);
    }
}
