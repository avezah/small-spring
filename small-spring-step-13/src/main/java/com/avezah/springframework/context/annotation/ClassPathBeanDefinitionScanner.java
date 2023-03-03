package com.avezah.springframework.context.annotation;

import cn.hutool.core.util.StrUtil;
import com.avezah.springframework.beans.factory.config.BeanDefinition;
import com.avezah.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.avezah.springframework.stereotype.Component;

import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                // 解析 bean 的作用域
                String beanScope = resolveBeanScope(candidate);
                if (StrUtil.isNotEmpty(beanScope)) {
                    candidate.setScope(beanScope);
                }
                // 生成 bean 的名称
                String beanName = determineBeanName(candidate);
                // 注册 bean
                registry.registerBeanDefinition(beanName, candidate);
            }
        }
    }

    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (scope != null) {
            return scope.value();
        }
        return StrUtil.EMPTY;
    }

    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component component = beanClass.getAnnotation(Component.class);
        String value = component.value();
        if (StrUtil.isEmpty(value)) {
            return StrUtil.lowerFirst(beanClass.getSimpleName());
        }
        return value;
    }

}
