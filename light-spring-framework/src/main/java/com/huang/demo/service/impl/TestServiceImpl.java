package com.huang.demo.service.impl;

import com.huang.demo.controller.TestController;
import com.huang.demo.service.TestService;
import com.huang.springframework.core.annotation.Autowired;
import com.huang.springframework.core.annotation.Service;

/**
 * @author: hsz
 * @date: 2021/3/19 10:30
 * @description:
 */

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestController testController;

    @Override
    public void print() {
        System.out.println("打印TestServiceImpl方法");
        System.out.println(testController.toString());
    }
}
