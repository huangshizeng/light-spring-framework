package com.huang.springframework.mvc.view;

import lombok.Data;

/**
 * @author: hsz
 * @date: 2021/3/26 09:42
 * @description:
 */

@Data
public class ModelAndViewContainer {

    private ModelAndView modelAndView = new ModelAndView();

    /**
     * 请求是否已经处理，true时表示用于响应json格式时，就没有model和view了，无需再进行后续的处理
     */
    private boolean requestHandled = false;
}
