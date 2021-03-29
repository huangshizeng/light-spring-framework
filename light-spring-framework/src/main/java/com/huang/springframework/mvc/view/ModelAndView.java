package com.huang.springframework.mvc.view;

import com.sun.istack.internal.Nullable;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: hsz
 * @date: 2021/3/25 15:52
 * @description:
 */

@Data
public class ModelAndView {

    private String view;

    private Map<String, Object> model = new HashMap<>();

    private Integer status;

    public ModelAndView addAttribute(String attributeName, @Nullable Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }
}
