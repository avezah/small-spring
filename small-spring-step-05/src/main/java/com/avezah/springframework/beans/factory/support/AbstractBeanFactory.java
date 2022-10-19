package com.avezah.springframework.beans.factory.support;

import com.avezah.springframework.beans.BeansException;
import com.avezah.springframework.beans.factory.BeanFactory;
import com.avezah.springframework.beans.factory.config.BeanDefinition;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
	@Override
	public Object getBean(String beanName) throws BeansException {
		return doGetBean(beanName, null);
	}

	@Override
	public Object getBean(String beanName, Object... args) throws BeansException{
		return doGetBean(beanName, args);
	}

	@Override
	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		return (T) getBean(name);
	}

	protected <T> T doGetBean(final String beanName, final Object[] args) {
		Object bean = super.getSingleton(beanName);
		if (bean != null){
			return (T) bean;
		}
		BeanDefinition beanDefinition = getBeanDefinition(beanName);
		return (T) createBean(beanName, beanDefinition, args);
	}

	protected abstract BeanDefinition getBeanDefinition(String BeanName) throws BeansException;

	protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;
}
