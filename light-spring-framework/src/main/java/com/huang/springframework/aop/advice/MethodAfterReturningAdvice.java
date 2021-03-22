package com.huang.springframework.aop.advice;

import java.lang.reflect.Method;

/**
 * @author: hsz
 * @date: 2021/3/22 10:08
 * @description:
 */

public interface MethodAfterReturningAdvice extends Advice {

    Object afterReturning(Object target, Method method, Object[] args, Object returnValue) throws Throwable;
}
