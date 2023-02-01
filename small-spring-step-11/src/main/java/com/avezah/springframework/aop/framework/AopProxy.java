package com.avezah.springframework.aop.framework;

public interface AopProxy {

    // 获取代理对象，有cglib和jdk两种实现
    Object getProxy();

}
