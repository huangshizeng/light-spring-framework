package com.huang.springframework.aop.advice;

import java.lang.reflect.Method;

/**
 * @author: hsz
 * @date: 2021/3/22 10:12
 * @description:
 */

public interface MethodAfterThrowingAdvice {

    void afterThrowing(Object target, Method method, Object[] args, Throwable throwable) throws Throwable;
}
