package com.avezah.springframework.aop;

public interface PointCut {

    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();
}
