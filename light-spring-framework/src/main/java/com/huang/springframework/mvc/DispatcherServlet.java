package com.huang.springframework.mvc;

import com.huang.springframework.core.support.AnnotationApplicationContext;
import com.huang.springframework.core.support.DefaultBeanFactory;
import com.huang.springframework.mvc.adapter.HandlerAdapter;
import com.huang.springframework.mvc.adapter.RequestMappingHandlerAdapter;
import com.huang.springframework.mvc.mapping.HandlerMapping;
import com.huang.springframework.mvc.mapping.HandlerMethod;
import com.huang.springframework.mvc.mapping.RequestMappingHandlerMapping;
import com.huang.springframework.mvc.view.JspView;
import com.huang.springframework.mvc.view.ModelAndView;
import com.huang.springframework.mvc.view.View;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: hsz
 * @date: 2021/3/23 18:31
 * @description: 前端控制器
 */

@WebServlet("/")
@Slf4j
public class DispatcherServlet extends HttpServlet {

    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;
    private View view;

    @SneakyThrows
    @Override
    public void init() {
        // 初始化容器
        log.info("容器初始化中。。。");
        DefaultBeanFactory beanFactory = new AnnotationApplicationContext();
        log.info("容器初始化成功");
        // 初始化HandlerMapping
        this.handlerMapping = new RequestMappingHandlerMapping(beanFactory);
        this.handlerAdapter = new RequestMappingHandlerAdapter();
        this.view = new JspView();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        try {
            doDispatch(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Handler the request fail", e);
        }
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerMethod handlerMethod = this.handlerMapping.getHandler(request);
        if (handlerMethod == null) {
            noHandlerFound(request, response);
            return;
        }
        ModelAndView mv = handlerAdapter.handle(request, response, handlerMethod);
        processDispatchResult(request, response, mv);
    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws ServletException, IOException {
        if (mv != null) {
            render(mv, request, response);
        }
    }

    /**
     * 渲染给定的ModelAndView
     */
    private void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (mv.getStatus() != null) {
            response.setStatus(mv.getStatus());
        }
        view.render(mv, request, response);
    }

    private void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.warn("No mapping for " + request.getMethod() + " " + request.getRequestURI());
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
