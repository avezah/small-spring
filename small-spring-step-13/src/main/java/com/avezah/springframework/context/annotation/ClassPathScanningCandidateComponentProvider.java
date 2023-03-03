package com.avezah.springframework.context.annotation;

import cn.hutool.core.util.ClassUtil;
import com.avezah.springframework.beans.factory.config.BeanDefinition;
import com.avezah.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

public class ClassPathScanningCandidateComponentProvider {

    // 找到所有的注解包含 Component 的类
    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
        for (Class<?> clazz : classes) {
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            candidates.add(beanDefinition);
        }
        return candidates;
    }
}
