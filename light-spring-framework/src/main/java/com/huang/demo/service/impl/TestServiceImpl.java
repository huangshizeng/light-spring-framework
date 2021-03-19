package com.huang.demo.service.impl;

import com.huang.demo.service.TestService;
import com.huang.springframework.core.annotation.Service;

/**
 * @author: hsz
 * @date: 2021/3/19 10:30
 * @description:
 */

@Service
public class TestServiceImpl implements TestService {

    @Override
    public void print() {
        System.out.println("打印TestServiceImpl方法");
    }
}
