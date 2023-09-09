package org.example.check;

import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class ClassFinderTest {
    /**
     * 测试获取某一包下面的class类
     */
    @Test
    public void getClassesInPackage() throws IOException, ClassNotFoundException {
        List<Class<?>> classesInPackage = ClassFinder.getClassesInPackage("org.example.entity.User");
        System.out.println(1);
    }
    @Test
    public void test(){
        String sBId = StrUtil.toUnderlineCase("sBId");
        System.out.println(sBId);
    }
}