package com.huang.springframework.aop;

import com.huang.springframework.aop.advice.MethodAfterReturningAdvice;
import com.huang.springframework.aop.advice.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: hsz
 * @date: 2021/3/22 11:19
 * @description:
 */

public abstract class AbstractAopProxy implements AopProxy {

    private Object target;
    private List<Advisor> advisorList;

    public AbstractAopProxy(Object target, List<Advisor> advisorList) {
        this.target = target;
        this.advisorList = advisorList;
    }

    public Object getTarget() {
        return target;
    }

    public List<Advisor> getAdvisorList() {
        return advisorList;
    }

    public List<Advisor> findAdvisorsThatCanApply(Method method) {
        List<Advisor> advisors = new ArrayList<>();
        for (Advisor advisor : advisorList) {
            if (advisor.getPointcut().matchsMethod(method)) {
                advisors.add(advisor);
            }
        }
        return advisors;
    }

    public void invokeBeforeAdvices(List<Advisor> advisorList, Method method, Object[] args) throws Throwable {
        for (Advisor advisor : advisorList) {
            if (advisor.getAdvice() instanceof MethodBeforeAdvice) {
                ((MethodBeforeAdvice) advisor.getAdvice()).before(target, method, args);
            }
        }
    }

    public Object invokeAfterAdvices(List<Advisor> advisorList, Method method, Object[] args, Object returnValue) throws Throwable {
        for (Advisor advisor : advisorList) {
            if (advisor.getAdvice() instanceof MethodAfterReturningAdvice) {
                returnValue = ((MethodAfterReturningAdvice) advisor.getAdvice()).afterReturning(target, method, args, returnValue);
            }
        }
        return returnValue;
    }
}
