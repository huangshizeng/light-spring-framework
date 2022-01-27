package com.huang.springframework.aop.processor;

import com.huang.springframework.aop.Advisor;
import com.huang.springframework.aop.AopProxyFactory;
import com.huang.springframework.aop.AspectJExpressionPointcut;
import com.huang.springframework.aop.advice.Advice;
import com.huang.springframework.aop.annotation.Aspect;
import com.huang.springframework.aop.annotation.Order;
import com.huang.springframework.core.config.BeanDefinition;
import com.huang.springframework.core.processor.EarlyReferenceBeanProcessor;
import com.huang.springframework.core.support.DefaultBeanFactory;
import com.sun.istack.internal.Nullable;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author: hsz
 * @date: 2021/3/19 11:38
 * @description:
 */

@Slf4j
public class AutoProxyCreator implements EarlyReferenceBeanProcessor {

    private DefaultBeanFactory beanFactory;
    /**
     * 缓存所有被@Aspect注解标记的bean名称
     */
    @Nullable
    private volatile List<String> aspectBeanNames;

    /**
     * 缓存所有的advisor
     */
    private final List<Advisor> advisorsCache = new ArrayList<>();

    private final Map<Object, Object> earlyProxyReferences = new ConcurrentHashMap<>(16);

    public AutoProxyCreator(DefaultBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws Exception {
        earlyProxyReferences.put(beanName, bean);
        return wrapIfNecessary(bean);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        if (bean != null && earlyProxyReferences.get(beanName) != null) {
            // 如果它适合被代理，则需要封装指定的bean
            return wrapIfNecessary(bean);
        }
        return earlyProxyReferences.remove(beanName);
    }

    private Object wrapIfNecessary(Object bean) throws Exception {
        // 如果是Advice，则跳过
        if (isInfrastructureClass(bean.getClass())) {
            return bean;
        }
        // 在此判断bean是否需要进行切面增强
        List<Advisor> matchAdvisors = getMatchedAdvisors(bean);
        if (matchAdvisors.size() > 0) {
            // 创建代理对象
            return createProxy(bean, matchAdvisors);
        }
        return bean;
    }

    private boolean isInfrastructureClass(Class<?> beanClass) {
        boolean retVal = Advice.class.isAssignableFrom(beanClass);
        if (retVal) {
            log.trace("Did not attempt to auto-proxy infrastructure class [" + beanClass.getName() + "]");
        }
        return retVal;
    }

    /**
     * 获取匹配的advisor
     *
     * @param bean bean实例
     * @return advisor列表
     */
    private List<Advisor> getMatchedAdvisors(Object bean) throws Exception {
        // 获取全部advisor
        List<Advisor> allAdvisors = findAllAdvisors();
        // 找到可以应用于该bean的advisor
        List<Advisor> matchedAdvisors = findAdvisorsThatCanApply(allAdvisors, bean.getClass());
        if (!matchedAdvisors.isEmpty()) {
            matchedAdvisors = sortAdvisors(matchedAdvisors);
        }
        return matchedAdvisors;
    }

    private List<Advisor> findAllAdvisors() throws Exception {
        List<String> aspectNames = this.aspectBeanNames;
        // 双重检验锁 保证线程安全
        if (aspectNames == null) {
            synchronized (this) {
                aspectNames = this.aspectBeanNames;
                if (aspectNames == null) {
                    List<Advisor> advisors = new ArrayList<>();
                    aspectNames = new ArrayList<>();
                    List<String> beanNames = beanFactory.getBeanDefinitionNames();
                    for (String beanName : beanNames) {
                        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
                        Class<?> clazz = beanDefinition.getBeanClass();
                        // 此处判断是否切面类
                        if (clazz.isAnnotationPresent(Aspect.class)) {
                            aspectNames.add(beanName);
                            Advisor advisor = getAdvisor(clazz, beanName);
                            advisors.add(advisor);
                            advisorsCache.add(advisor);
                        }
                    }
                    this.aspectBeanNames = aspectNames;
                    return advisors;
                }
            }
        }
        return advisorsCache;
    }

    /**
     * 找到可用于目标类的advisor
     *
     * @param allAdvisors 全部advisor
     * @param clazz       目标类
     * @return 可用于目标类的advisor
     */
    private List<Advisor> findAdvisorsThatCanApply(List<Advisor> allAdvisors, Class<?> clazz) {
        List<Advisor> advisors = new ArrayList<>();
        Method[] methods = clazz.getDeclaredMethods();
        // 先跟类匹配，再跟类中的方法匹配，只要有一个方法匹配就表示可以应用于该bean
        for (Advisor advisor : allAdvisors) {
            AspectJExpressionPointcut pointcut = advisor.getPointcut();
            if (pointcut.matchsClass(clazz)) {
                for (Method method : methods) {
                    if (pointcut.matchsMethod(method)) {
                        advisors.add(advisor);
                        break;
                    }
                }
            }
        }
        return advisors;
    }

    private Advisor getAdvisor(Class<?> clazz, String beanName) throws Exception {
        Aspect aspect = clazz.getAnnotation(Aspect.class);
        if (clazz.isAnnotationPresent(Order.class)) {
            return new Advisor(clazz.getAnnotation(Order.class).value(), (Advice) beanFactory.getBean(beanName), aspect.value());
        }
        return new Advisor((Advice) beanFactory.getBean(beanName), aspect.value());
    }

    private List<Advisor> sortAdvisors(List<Advisor> matchedAdvisors) {
        return matchedAdvisors.stream().sorted(Comparator.comparing(Advisor::getOrder)).collect(Collectors.toList());
    }

    private Object createProxy(Object bean, List<Advisor> matchAdvisors) {
        return AopProxyFactory.createAopProxy(bean, matchAdvisors).getProxy();
    }
}
