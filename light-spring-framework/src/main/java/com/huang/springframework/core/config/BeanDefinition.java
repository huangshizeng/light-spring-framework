package com.huang.springframework.core.config;


import com.sun.istack.internal.Nullable;

/**
 * bean定义接口，BeanDefinition描述了一个bean实例
 *
 * @author hsz
 * @data 2020/6/17 09:25:44
 */
public interface BeanDefinition {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    /**
     * 指定bean定义中的bean类名
     *
     * @param beanClass bean类实例
     */
    void setBeanClass(Class<?> beanClass);

    /**
     * 获取bean类名
     *
     * @return bean类实例
     */
    Class<?> getBeanClass();

    void setScope(@Nullable String scope);

    String getScope();

    void setLazyInit(boolean lazyInit);

    boolean isLazyInit();

    /**
     * 指定要使用的工厂bean（如果有）
     *
     * @param factoryBeanName 工厂bean名称，成员工厂方法的方式创建bean时，需要告诉bean工厂怎么获取工厂bean名
     */
    void setFactoryBeanName(@Nullable String factoryBeanName);

    String getFactoryBeanName();

    /**
     * 指定工厂方法（如果有），将使用构造函数参数调用此方法，如果未指定，则不使用任何参数。
     * 该方法将在指定的工厂bean（如果有）上被调用，否则将作为本地bean类上的静态方法被调用
     *
     * @param factoryMethodName 工厂方法名，静态工厂方法的方式创建bean时，需要告诉bean工厂怎么获取工厂方法名
     */
    void setFactoryMethodName(String factoryMethodName);

    String getFactoryMethodName();

    /**
     * 设置bean初始化方法名
     *
     * @param initMethodName 初始化方法名
     */
    void setInitMethodName(String initMethodName);

    String getInitMethodName();

    /**
     * 设置bean销毁方法名
     *
     * @param destroyMethodName 销毁方法名
     */
    void setDestroyMethodName(String destroyMethodName);

    String getDestroyMethodName();

    boolean isSingleton();

    boolean isPrototype();
}
