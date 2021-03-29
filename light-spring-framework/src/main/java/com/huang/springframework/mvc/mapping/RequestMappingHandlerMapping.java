package com.huang.springframework.mvc.mapping;

import cn.hutool.core.util.StrUtil;
import com.huang.springframework.core.annotation.Controller;
import com.huang.springframework.core.support.DefaultBeanFactory;
import com.huang.springframework.mvc.annotation.RequestMapping;
import com.huang.springframework.mvc.annotation.RequestMethod;
import com.huang.springframework.mvc.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: hsz
 * @date: 2021/3/25 09:53
 * @description:
 */

public class RequestMappingHandlerMapping implements HandlerMapping {

    private DefaultBeanFactory beanFactory;

    /**
     * 存放映射信息
     */
    private Map<RequestMappingInfo, HandlerMethod> handlerMap = new ConcurrentHashMap<>();

    public RequestMappingHandlerMapping(DefaultBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        initHandlerMethods();
    }

    private void initHandlerMethods() {
        Collection<Object> beanList = beanFactory.getBeanMap().values();
        beanList.forEach(bean -> {
            if (isHandler(bean)) {
                detectHandlerMethods(bean);
            }
        });
    }

    private boolean isHandler(Object bean) {
        Class<?> clazz = bean.getClass();
        return clazz.isAnnotationPresent(Controller.class);
    }

    private void detectHandlerMethods(Object handler) {
        String baseUrl = "";
        Class<?> clazz = handler.getClass();
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            baseUrl = clazz.getAnnotation(RequestMapping.class).value();
        }
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMappingInfo info = getMappingForMethod(baseUrl, method);
                HandlerMethod handlerMethod = createHandlerMethod(handler, clazz, method);
                handlerMap.put(info, handlerMethod);
            }
        }
    }

    private RequestMappingInfo getMappingForMethod(String baseUrl, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String methodUrl = baseUrl + requestMapping.value();
        RequestMethod requestMethod = requestMapping.method();
        return new RequestMappingInfo(methodUrl, String.valueOf(requestMethod));
    }

    private HandlerMethod createHandlerMethod(Object handler, Class<?> clazz, Method method) {
        Map<String, Class<?>> parameterMap = getParametersFromMethod(method);
        return new HandlerMethod(clazz, handler, method, parameterMap);
    }

    private Map<String, Class<?>> getParametersFromMethod(Method method) {
        Parameter[] parameters = method.getParameters();
        Map<String, Class<?>> parameterMap = new LinkedHashMap<>(parameters.length);
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                if (StrUtil.isNotEmpty(requestParam.value())) {
                    parameterMap.put(requestParam.value(), parameter.getType());
                    continue;
                }
            }
            parameterMap.put(parameter.getName(), parameter.getType());
        }
        return parameterMap;
    }

    @Override
    public HandlerMethod getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        RequestMappingInfo info = new RequestMappingInfo(uri, method);
        return handlerMap.get(info);
    }
}
