package com.huang.springframework.mvc.adapter;

import com.huang.springframework.mvc.mapping.HandlerMethod;
import com.huang.springframework.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: hsz
 * @date: 2021/3/25 15:51
 * @description:
 */

public interface HandlerAdapter {

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws InvocationTargetException, IllegalAccessException, Exception;
}
