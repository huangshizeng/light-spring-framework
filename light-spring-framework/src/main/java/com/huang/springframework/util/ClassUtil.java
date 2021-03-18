package com.huang.springframework.util;

import cn.hutool.core.io.resource.ClassPathResource;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author: hsz
 * @date: 2021/3/18 15:05
 * @description:
 */

public class ClassUtil {

    /**
     * 解析类资源文件
     *
     * @param propertiesPath 资源路径
     * @return Properties对象
     */
    public static Properties resolveClassPathResource(String propertiesPath) throws IOException {
        ClassPathResource resource = new ClassPathResource(propertiesPath);
        Properties properties = new Properties();
        properties.load(resource.getStream());
        return properties;
    }

    /**
     * 获取包下类集合
     *
     * @param packageName 包名
     * @return 类集合
     */
    public static Set<Class> extractPackageClass(String packageName) {
        Set<Class> classSet = new HashSet<>();
        // 通过类加载器获取到加载的资源
        URL url = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "/"));
        if (url == null) {
            return classSet;
        }
        File packageDirectory = new File(url.getPath());
        extractClassFile(classSet, packageDirectory, packageName);
        return classSet;
    }

    /**
     * 递归获取目标package里面的所有class文件(包括子package里的class文件)
     *
     * @param emptyClassSet 装载目标类的集合
     * @param fileSource    目录
     * @param packageName   包名
     * @return 类集合
     */
    private static void extractClassFile(final Set<Class> emptyClassSet, File fileSource, final String packageName) {
        if (!fileSource.isDirectory()) {
            return;
        }
        //如果是一个文件夹，则调用其listFiles方法获取文件夹下的文件或文件夹
        File[] files = fileSource.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                } else {
                    //获取文件的绝对值路径
                    String absoluteFilePath = file.getAbsolutePath();
                    if (absoluteFilePath.endsWith(".class")) {
                        //若是class文件，则直接加载
                        addToClassSet(absoluteFilePath);
                    }
                }
                return false;
            }

            // 根据class文件的绝对值路径，获取并生成class对象，并放入classSet中
            private void addToClassSet(String absoluteFilePath) {
                // 从class文件的绝对值路径里提取出包含了package的类名
                absoluteFilePath = absoluteFilePath.replace(File.separator, ".");
                String className = absoluteFilePath.substring(absoluteFilePath.indexOf(packageName));
                className = className.substring(0, className.lastIndexOf("."));
                // 通过反射机制获取对应的Class对象并加入到classSet里
                Class targetClass = loadClass(className);
                emptyClassSet.add(targetClass);
            }
        });
        if (files != null) {
            for (File f : files) {
                //递归调用
                extractClassFile(emptyClassSet, f, packageName);
            }
        }
    }

    /**
     * 获取Class对象
     *
     * @param className class全名=package + 类名
     * @return Class
     */
    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
