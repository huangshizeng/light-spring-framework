package com.huang.demo.aspect;

import com.huang.springframework.aop.advice.MethodBeforeAdvice;
import com.huang.springframework.aop.annotation.Aspect;

import java.lang.reflect.Method;

/**
 * @author: hsz
 * @date: 2021/3/22 11:37
 * @description:
 */

@Aspect("execution(* com.huang.demo.controller.TestController.*(..))")
public class LogBefore implements MethodBeforeAdvice {

    @Override
    public void before(Object target, Method method, Object[] args) throws Throwable {
        System.out.println("【LogBefore】方法调用前:");
    }
}
