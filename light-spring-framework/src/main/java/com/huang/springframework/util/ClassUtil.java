package com.huang.springframework.util;

import cn.hutool.core.io.resource.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

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
}
