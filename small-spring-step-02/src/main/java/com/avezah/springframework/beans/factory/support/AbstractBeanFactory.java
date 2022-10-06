package com.avezah.springframework.beans.factory.support;

import com.avezah.springframework.beans.BeansException;
import com.avezah.springframework.beans.factory.config.BeanDefinition;
import com.avezah.springframework.beans.factory.BeanFactory;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
	@Override
	public Object getBean(String beanName) throws BeansException {
		Object bean = super.getSingleton(beanName);
		if (bean != null) {
			return bean;
		}

		BeanDefinition beanDefinition = this.getBeanDefinition(beanName);
		return createBean(beanName, beanDefinition);
	}

	protected abstract BeanDefinition getBeanDefinition(String BeanName) throws BeansException;

	protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException;
}
