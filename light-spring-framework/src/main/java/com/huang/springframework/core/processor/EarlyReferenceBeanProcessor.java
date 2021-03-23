package com.huang.springframework.core.processor;

/**
 * @author: hsz
 * @date: 2021/3/23 09:51
 * @description:
 */

public interface EarlyReferenceBeanProcessor extends BeanPostProcessor {

    default Object getEarlyBeanReference(Object bean, String beanName) throws Exception {
        return bean;
    }
}
