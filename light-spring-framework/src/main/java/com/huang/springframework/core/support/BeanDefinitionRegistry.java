package com.huang.springframework.core.support;


import com.huang.springframework.core.config.BeanDefinition;

/**
 * bean定义注册接口，通过该接口实例将BeanDefinition注册到bean工厂中
 *
 * @author hsz
 * @data 2020/6/17 09:22:51
 */
public interface BeanDefinitionRegistry {

    /**
     * 注册BeanDefinition
     *
     * @param beanName       bean名称
     * @param beanDefinition bean定义
     * @throws Exception
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception;

    /**
     * 移除BeanDefinition
     *
     * @param beanName bean名称
     * @throws Exception 没有该bean定义
     */
    void removeBeanDefinition(String beanName) throws Exception;

    /**
     * 获取BeanDefinition
     *
     * @param beanName bean名称
     * @return BeanDefinition实例
     * @throws Exception 没有该bean定义
     */
    BeanDefinition getBeanDefinition(String beanName) throws Exception;

    /**
     * 是否包含指定name的bean定义
     *
     * @param beanName bean名称
     * @return true 包含该bean定义
     */
    boolean containsBeanDefinition(String beanName);
}
