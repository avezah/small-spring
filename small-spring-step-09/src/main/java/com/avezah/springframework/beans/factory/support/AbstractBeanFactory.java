package com.avezah.springframework.beans.factory.support;

import com.avezah.springframework.beans.BeansException;
import com.avezah.springframework.beans.factory.FactoryBean;
import com.avezah.springframework.beans.factory.config.BeanDefinition;
import com.avezah.springframework.beans.factory.config.BeanPostProcessor;
import com.avezah.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

	private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

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
		Object sharedInstance = getSingleton(beanName);
		if (sharedInstance != null){
			return (T) getObjectForBeanInstance(sharedInstance, beanName);
		}
		BeanDefinition beanDefinition = getBeanDefinition(beanName);
		Object bean = createBean(beanName, beanDefinition, args);
		return (T) getObjectForBeanInstance(bean, beanName);
	}

	private Object getObjectForBeanInstance(Object beanInstance, String beanName) {
		// 如果是普通bean，直接返回
		if (!(beanInstance instanceof FactoryBean)) {
			return beanInstance;
		}
		// 如果是factory bean，使用factory bean获取bean
		Object object = getCachedObjectForFactoryBean(beanName);
		if (object == null) {
			FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
			object = getObjectFromFactoryBean(factoryBean, beanName);
		}
		return object;
	}

	@Override
	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
		this.beanPostProcessors.remove(beanPostProcessor);
		this.beanPostProcessors.add(beanPostProcessor);
	}

	public List<BeanPostProcessor> getBeanPostProcessors() {
		return this.beanPostProcessors;
	}

	protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

	protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;
}
