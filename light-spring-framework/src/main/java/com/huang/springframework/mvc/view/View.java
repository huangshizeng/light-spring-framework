package com.huang.springframework.mvc.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: hsz
 * @date: 2021/3/29 11:14
 * @description:
 */

public interface View {

    void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
