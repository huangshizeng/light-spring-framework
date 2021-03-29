package com.huang.springframework.mvc.adapter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.huang.springframework.mvc.annotation.ResponseBody;
import com.huang.springframework.mvc.mapping.HandlerMethod;
import com.huang.springframework.mvc.view.ModelAndView;
import com.huang.springframework.mvc.view.ModelAndViewContainer;
import com.huang.springframework.util.ConverterUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: hsz
 * @date: 2021/3/25 17:12
 * @description:
 */

public class RequestMappingHandlerAdapter implements HandlerAdapter {

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception {
        ModelAndViewContainer container = new ModelAndViewContainer();
        invokeAndHandle(request, response, handler, container);
        return getModelAndView(container);
    }

    private ModelAndView getModelAndView(ModelAndViewContainer container) {
        return container.isRequestHandled() ? null : container.getModelAndView();
    }

    private void invokeAndHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, ModelAndViewContainer container) throws InvocationTargetException, IllegalAccessException, IOException {
        List<Object> args = getMethodArgumentValues(request, handler, container);
        Object returnValue = doinvoke(handler, args);
        if (returnValue == null) {
            container.setRequestHandled(true);
            return;
        }
        handleReturnValue(returnValue, request, response, handler, container);
    }

    private void handleReturnValue(Object returnValue, HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, ModelAndViewContainer container) throws IOException {
        if (handler.getMethod().isAnnotationPresent(ResponseBody.class)) {
            response.getWriter().write(JSONUtil.toJsonStr(returnValue));
            container.setRequestHandled(true);
        } else if (CharSequence.class.isAssignableFrom(returnValue.getClass())) {
            container.getModelAndView().setView((String) returnValue);
        }
    }

    private Object doinvoke(HandlerMethod handler, List<Object> args) throws InvocationTargetException, IllegalAccessException {
        return handler.getMethod().invoke(handler.getBean(), args.toArray());
    }

    private List<Object> getMethodArgumentValues(HttpServletRequest request, HandlerMethod handler, ModelAndViewContainer container) {
        Map<String, Class<?>> parameters = handler.getParameters();
        List<Object> args = new ArrayList<>(parameters.size());
        parameters.forEach((name, clazz) -> {
            if (ModelAndView.class.isAssignableFrom(clazz)) {
                args.add(container.getModelAndView());
            } else {
                String parameter = request.getParameter(name);
                // 没找到参数就使用该类型的默认空值
                if (StrUtil.isEmpty(parameter)) {
                    args.add(ConverterUtil.primitiveNull(clazz));
                } else {
                    args.add(ConverterUtil.convert(clazz, parameter));
                }
            }
        });
        return args;
    }
}
