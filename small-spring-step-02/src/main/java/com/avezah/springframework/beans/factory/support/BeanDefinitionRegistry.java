package com.avezah.springframework.beans.factory.support;

import com.avezah.springframework.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {

	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
