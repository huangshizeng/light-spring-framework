package com.huang.springframework.aop;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;

/**
 * @author: hsz
 * @date: 2021/3/19 16:25
 * @description:
 */

public class AspectJExpressionPointcut {

    private static PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingAllPrimitivesAndUsingContextClassloaderForResolution();

    private String expression;

    private PointcutExpression pointcutExpression;

    public AspectJExpressionPointcut(String expression) {
        this.expression = expression;
        pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }

    /**
     * 匹配类
     *
     * @param targetClass 目标类
     * @return 是否匹配
     */
    public boolean matchsClass(Class<?> targetClass) {
        return pointcutExpression.couldMatchJoinPointsInType(targetClass);
    }

    public boolean matchsMethod(Method method) {
        ShadowMatch sm = pointcutExpression.matchesMethodExecution(method);
        return sm.alwaysMatches();
    }
}
