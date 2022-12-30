package com.avezah.springframework.beans.factory;

import com.avezah.springframework.beans.BeansException;

public interface BeanFactory {

	Object getBean(String beanName) throws BeansException;

	Object getBean(String beanName, Object... args) throws BeansException;

	<T> T getBean(String name, Class<T> requiredType) throws BeansException;
}
