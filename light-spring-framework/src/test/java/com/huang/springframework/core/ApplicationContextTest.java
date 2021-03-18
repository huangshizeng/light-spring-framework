package com.huang.springframework.core;

import com.huang.springframework.core.support.AnnotationApplicationContext;
import org.junit.jupiter.api.Test;

/**
 * @author: hsz
 * @date: 2021/3/18 17:03
 * @description:
 */

public class ApplicationContextTest {

    @Test
    public void test() throws Exception {
        AnnotationApplicationContext applicationContext = new AnnotationApplicationContext();
        System.out.println(applicationContext.getBean("testController"));
    }
}
