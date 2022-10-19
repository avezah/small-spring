package com.avezah.springframework.beans.factory.support;

import com.avezah.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

	private Map<String, Object> singletonObjects = new HashMap<>();

	@Override
	public Object getSingleton(String beanName) {
		return this.singletonObjects.get(beanName);
	}

	protected void addSingleton(String beanName, Object bean) {
		this.singletonObjects.put(beanName, bean);
	}
}
