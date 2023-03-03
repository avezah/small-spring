package com.avezah.springframework.aop;

public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();
}
