package com.huang.springframework.aop.advice;

import java.lang.reflect.Method;

/**
 * @author: hsz
 * @date: 2021/3/22 10:06
 * @description:
 */

public interface MethodBeforeAdvice extends Advice {

    void before(Object target, Method method, Object[] args) throws Throwable;
}
