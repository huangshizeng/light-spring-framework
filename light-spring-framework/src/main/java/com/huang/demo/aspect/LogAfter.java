package com.huang.demo.aspect;

import com.huang.springframework.aop.advice.MethodAfterReturningAdvice;
import com.huang.springframework.aop.annotation.Order;

import java.lang.reflect.Method;

/**
 * @author: hsz
 * @date: 2021/3/22 14:17
 * @description:
 */

//@Aspect("execution(* com.huang.demo.controller.TestController.*(..))")
@Order
public class LogAfter implements MethodAfterReturningAdvice {

    @Override
    public Object afterReturning(Object target, Method method, Object[] args, Object returnValue) throws Throwable {
        System.out.println("【LogAfter】方法调用后:");
        return null;
    }
}
