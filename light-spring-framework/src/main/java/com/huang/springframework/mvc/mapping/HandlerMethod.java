package com.huang.springframework.mvc.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author: hsz
 * @date: 2021/3/25 10:56
 * @description:
 */

@Data
@AllArgsConstructor
public class HandlerMethod {

    private Class clazz;
    private Object bean;
    private Method method;
    private Map<String, Class<?>> parameters;
}
