package com.huang.springframework.core.support;


import cn.hutool.core.util.StrUtil;
import com.huang.springframework.aop.annotation.Aspect;
import com.huang.springframework.core.annotation.Component;
import com.huang.springframework.core.annotation.Controller;
import com.huang.springframework.core.annotation.Service;
import com.huang.springframework.core.config.BeanDefinition;
import com.huang.springframework.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hsz
 * @data 2020/6/18 16:24:51
 */

@Slf4j
public class AnnotationApplicationContext extends DefaultBeanFactory {

    private Properties properties;
    /**
     * 标记为bean的注解列表
     */
    private final List<Class<? extends Annotation>> BEAN_ANNOTATION = Arrays.asList(Controller.class, Service.class, Component.class, Aspect.class);

    public AnnotationApplicationContext(String propertiesPath) throws Exception {
        this.properties = ClassUtil.resolveClassPathResource(propertiesPath);
        refresh();
    }

    public AnnotationApplicationContext() throws Exception {
        this("application.properties");
    }

    public void refresh() {
        registerBeanPostProcessors(this);
        Set<Class<?>> classSet = doScan();
        loadBeanDefinitions(classSet);
        finishBeanInitialization();
    }

    private Set<Class<?>> doScan() {
        String packageName = properties.getProperty("scanPackage");
        if (packageName == null) {
            log.error("没有配置包扫描路径");
            return new HashSet<>();
        }
        return cn.hutool.core.util.ClassUtil.scanPackage(packageName);
    }

    private void loadBeanDefinitions(Set<Class<?>> classSet) {
        for (Class clazz : classSet) {
            for (Class<? extends Annotation> annotation : BEAN_ANNOTATION) {
                //如果类上面标记了定义的注解
                if (clazz.isAnnotationPresent(annotation)) {
                    BeanDefinition beanDefinition = new GenericBeanDefinition();
                    beanDefinition.setBeanClass(clazz);
                    beanDefinition.setScope("singleton");
                    beanDefinition.setLazyInit(false);
                    registerBeanDefinition(StrUtil.lowerFirst(clazz.getSimpleName()), beanDefinition);
                    break;
                }
            }
        }
    }

    public void finishBeanInitialization() {
        Map<String, BeanDefinition> beanDefinitionMap = getBeanDefinitionMap();
        beanDefinitionMap.forEach((name, beanDefinition) -> {
            if ("singleton".equals(beanDefinition.getScope()) && !beanDefinition.isLazyInit()) {
                try {
                    getBean(name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public List<Class<?>> getClassByAnnotation(Class<? extends Annotation> annotation) {
        List<Class<?>> clazzList = new ArrayList<>();
        List<? extends Class<?>> collect = getBeanDefinitionMap().values().stream()
                .map(BeanDefinition::getBeanClass)
                .collect(Collectors.toList());
        for (Class clazz : collect) {
            if (clazz.isAnnotationPresent(annotation)) {
                clazzList.add(clazz);
            }
        }
        return clazzList;
    }
}
