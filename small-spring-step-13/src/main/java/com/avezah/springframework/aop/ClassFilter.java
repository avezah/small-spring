package com.avezah.springframework.aop;

public interface ClassFilter {

    boolean matches(Class<?> clazz);

}
