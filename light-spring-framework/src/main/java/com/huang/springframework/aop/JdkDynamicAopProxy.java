package com.huang.springframework.aop;

import cn.hutool.core.collection.CollectionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author: hsz
 * @date: 2021/3/19 17:22
 * @description:
 */

public class JdkDynamicAopProxy extends AbstractAopProxy implements InvocationHandler {

    public JdkDynamicAopProxy(Object target, List<Advisor> advisorList) {
        super(target, advisorList);
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(getTarget().getClass().getClassLoader(), getTarget().getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Advisor> advisorList = findAdvisorsThatCanApply(method);
        if (CollectionUtil.isEmpty(advisorList)) {
            return method.invoke(getTarget(), args);
        }
        invokeBeforeAdvices(advisorList, method, args);
        try {
            Object returnValue = method.invoke(getTarget(), args);
            return invokeAfterAdvices(advisorList, method, args, returnValue);
        } catch (Exception ex) {
            invokeAfterThrowingAdvices(advisorList, method, args, ex);
        }
        return null;
    }
}
