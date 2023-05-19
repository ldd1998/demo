package org.example.parameter;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * 值传递引用传递测试
 */
public class ParameterTest {
    public void booleanToFalse(Boolean flag){
        flag = false;
    }
    public void integer0To1(Integer intValue){
        intValue = 1;
    }

    /**
     * 测试方法是否可以把Boolean类型改变其值
     * 测试结果为不可以，因为Java都是值传递
     * 当传对象的时候因为传入的值是地址，所以效果类似引用传递
     * 因为String被final修饰所以也表现出值传递特性
     * 可使用类似AtomicBoolean包装类来解决
     */
    @Test
    public void test01(){
        Boolean flag = true;
        booleanToFalse(flag);
        Assert.assertTrue(flag);
    }

    @Test
    public void test02(){
        Integer integerValue = new Integer(0);
        integer0To1(integerValue);
        System.out.println(integerValue);
    }
}
