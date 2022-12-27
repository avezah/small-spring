package com.avezah.springframework.beans.factory.support;

import com.avezah.springframework.beans.BeansException;
import com.avezah.springframework.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {

	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

	BeanDefinition getBeanDefinition(String beanName) throws BeansException;

	boolean containsBeanDefinition(String beanName);

	String[] getBeanDefinitionNames();
}
