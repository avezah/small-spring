package com.avezah.springframework.test.common;

import com.avezah.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class SenderBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("BeforeAdvice: do something before send");
    }
}
