package com.avezah.springframework.test.common;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class SenderInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        System.out.println("Before send");
        Object result = methodInvocation.proceed();
        System.out.println("After send");
        return result;
    }
}
