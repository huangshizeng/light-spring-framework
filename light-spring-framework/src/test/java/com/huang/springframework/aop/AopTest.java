package com.huang.springframework.aop;

import com.huang.demo.controller.TestController;
import com.huang.springframework.core.support.AnnotationApplicationContext;
import org.junit.jupiter.api.Test;

/**
 * @author: hsz
 * @date: 2021/3/22 11:35
 * @description:
 */

public class AopTest {

    @Test
    public void test() throws Exception {
        AnnotationApplicationContext applicationContext = new AnnotationApplicationContext();
        TestController testController = (TestController) applicationContext.getBean("testController");
        testController.print("abc", 2);
    }
}
