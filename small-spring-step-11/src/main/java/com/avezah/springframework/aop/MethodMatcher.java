package com.avezah.springframework.aop;

import java.lang.reflect.Method;

public interface MethodMatcher {

    boolean matches(Method method, Class<?> clazz);

}
