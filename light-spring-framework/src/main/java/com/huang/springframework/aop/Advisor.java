package com.huang.springframework.aop;

import com.huang.springframework.aop.advice.Advice;
import lombok.Data;

/**
 * @author: hsz
 * @date: 2021/3/19 14:40
 * @description:
 */

@Data
public class Advisor {

    /**
     * 默认排序值为1000
     */
    private int order = 1000;

    private Advice advice;

    private String express;

    private AspectJExpressionPointcut pointcut;

    public Advisor(Advice advice, String express) {
        this.advice = advice;
        this.express = express;
        this.pointcut = new AspectJExpressionPointcut(express);
    }

    public Advisor(int order, Advice advice, String express) {
        this.order = order;
        this.advice = advice;
        this.express = express;
        this.pointcut = new AspectJExpressionPointcut(express);
    }
}
