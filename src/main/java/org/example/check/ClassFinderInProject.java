package org.example.check;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassFinderInProject {
    public static void main(String[] args) {
        List<Class<?>> classesWithTableName = findClassesWithAnnotationInPackage("", TableName.class);

        for (Class<?> clazz : classesWithTableName) {
            System.out.println("Class with @TableName: " + clazz.getName());
        }
    }

    public static List<Class<?>> findClassesWithAnnotationInPackage(String packageName, Class<? extends Annotation> annotationClass) {
        List<Class<?>> classes = new ArrayList<>();
        String packagePath = packageName.replace('.', '/');

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources(packagePath);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File packageDir = new File(resource.toURI());
                classes.addAll(findClassesWithAnnotationInDirectory(packageName, packageDir, annotationClass));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classes;
    }

    private static List<Class<?>> findClassesWithAnnotationInDirectory(String packageName, File directory, Class<? extends Annotation> annotationClass) {
        List<Class<?>> classes = new ArrayList<>();

        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClassesWithAnnotationInDirectory(packageName + "." + file.getName(), file, annotationClass));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz = Class.forName(className.substring(1,className.length()));
                    if (clazz.isAnnotationPresent(annotationClass)) {
                        classes.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return classes;
    }
}
