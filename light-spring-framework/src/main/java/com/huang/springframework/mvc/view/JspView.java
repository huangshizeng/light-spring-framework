package com.huang.springframework.mvc.view;

import cn.hutool.core.util.ObjectUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author: hsz
 * @date: 2021/3/29 11:21
 * @description:
 */

public class JspView implements View {

    private final String PREFIX = "/WEB-INF/views";
    private final String SUFFIX = ".jsp";

    @Override
    public void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        exposeModelAsRequestAttributes(mv.getModel(), request);
        String path = PREFIX + mv.getView() + SUFFIX;
        request.getRequestDispatcher(path).forward(request, response);
    }

    private void exposeModelAsRequestAttributes(Map<String, Object> model, HttpServletRequest request) {
        model.forEach((name, value) -> {
            if (ObjectUtil.isNotNull(value)) {
                request.setAttribute(name, value);
            }
        });
    }
}
