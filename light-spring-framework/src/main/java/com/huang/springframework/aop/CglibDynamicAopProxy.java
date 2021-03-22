package com.huang.springframework.aop;

import cn.hutool.core.collection.CollectionUtil;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author: hsz
 * @date: 2021/3/22 11:13
 * @description:
 */

public class CglibDynamicAopProxy extends AbstractAopProxy implements MethodInterceptor {

    public CglibDynamicAopProxy(Object target, List<Advisor> advisors) {
        super(target, advisors);
    }

    @Override
    public Object getProxy() {
        return Enhancer.create(getTarget().getClass(), this);
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        List<Advisor> advisorList = findAdvisorsThatCanApply(method);
        if (CollectionUtil.isEmpty(advisorList)) {
            return methodProxy.invokeSuper(getTarget(), args);
        }
        invokeBeforeAdvices(advisorList, method, args);
        try {
            Object returnValue = method.invoke(getTarget(), args);
            return invokeAfterAdvices(advisorList, method, args, returnValue);
        } catch (Exception ex) {
            ex.printStackTrace();
            invokeAfterThrowingAdvices(advisorList, method, args, ex);
        }
        return null;
    }
}
