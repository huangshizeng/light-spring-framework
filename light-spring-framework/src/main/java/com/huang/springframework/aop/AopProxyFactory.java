package com.huang.springframework.aop;

import java.util.List;

/**
 * @author: hsz
 * @date: 2021/3/19 17:17
 * @description:
 */

public class AopProxyFactory {

    public static AopProxy createAopProxy(Object target, List<Advisor> advisors) {
        // jdk
        if (target.getClass().getInterfaces().length > 0) {
            return new JdkDynamicAopProxy(target, advisors);
        } else {
            return new CglibDynamicAopProxy(target, advisors);
        }
    }
}
