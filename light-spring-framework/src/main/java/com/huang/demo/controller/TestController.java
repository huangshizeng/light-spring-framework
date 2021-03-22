package com.huang.demo.controller;

import com.huang.demo.service.TestService;
import com.huang.springframework.core.annotation.Autowired;
import com.huang.springframework.core.annotation.Controller;

/**
 * @author: hsz
 * @date: 2021/3/18 18:12
 * @description:
 */

@Controller
public class TestController {

    @Autowired
    private TestService testService;

    public void print(String p1, int p2) {
        testService.print();
    }
}
