package com.huang.springframework.mvc.mapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: hsz
 * @date: 2021/3/24 16:46
 * @description:
 */

public interface HandlerMapping {

    HandlerMethod getHandler(HttpServletRequest request);
}
