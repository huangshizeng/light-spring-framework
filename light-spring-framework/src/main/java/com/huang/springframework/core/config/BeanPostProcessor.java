package com.huang.springframework.core.config;

import com.sun.istack.internal.Nullable;

/**
 * @author: hsz
 * @date: 2021/3/19 09:23
 * @description: bean后置处理器接口
 */

public interface BeanPostProcessor {

    @Nullable
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Nullable
    default Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
}
