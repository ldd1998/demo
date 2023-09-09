package org.example.check;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 查找并返回指定包内的所有的类
 * @author ldd
 */
@Slf4j
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
                addClassesInJar(packagePath,resource,classes);
            }
        }
        return classes;
    }

    private static void addClassesInJar(String packagePath, URL resource, List<Class<?>> classes) throws IOException, ClassNotFoundException {
        JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
        JarFile jarFile = jarURLConnection.getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();

            if (entryName.endsWith(".class") && entryName.startsWith(packagePath)) {
                String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
                Class<?> clazz = Class.forName(className);
                classes.add(clazz);
            }
        }
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
