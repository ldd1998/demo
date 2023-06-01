package org.example.check;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassFinder {
    public static List<Class<?>> getClassesInPackage(String packageName) throws ClassNotFoundException, IOException {
        List<Class<?>> classes = new ArrayList<>();

        String packagePath = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(packagePath);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();

            if (protocol.equals("file")) {
                String path = resource.getPath();
                addClassesInPath(packageName, path, classes);
            } else if (protocol.equals("jar")) {
                // 处理jar文件中的类
                // 你可以根据需要添加对jar文件中类的处理逻辑
            }
        }

        return classes;
    }

    private static void addClassesInPath(String packageName, String path, List<Class<?>> classes) throws ClassNotFoundException {
        File dir = new File(path);
        File[] files = dir.listFiles();

        for (File file : files) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                String subPackageName = packageName + "." + fileName;
                String subPackagePath = path + "/" + fileName;
                addClassesInPath(subPackageName, subPackagePath, classes);
            } else if (fileName.endsWith(".class")) {
                String className = fileName.substring(0, fileName.length() - 6);
                String fullClassName = packageName + "." + className;
                Class<?> cls = Class.forName(fullClassName);
                classes.add(cls);
            }
        }
    }
}
