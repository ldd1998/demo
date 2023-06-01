package org.example.check;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class ClassFinderTest {
    /**
     * 测试获取某一包下面的class类
     */
    @Test
    public void getClassesInPackage() throws IOException, ClassNotFoundException {
        List<Class<?>> classesInPackage = ClassFinder.getClassesInPackage("org.example.entity.User");
        System.out.println(1);
    }
}