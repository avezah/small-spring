package com.avezah.springframework.beans.factory.support;

import com.avezah.springframework.beans.BeansException;
import com.avezah.springframework.beans.factory.DisposableBean;
import com.avezah.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

	protected Object NULL_OBJECT = new Object();

	private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

	private Map<String, DisposableBean> disposableBeans = new ConcurrentHashMap<>();

	@Override
	public Object getSingleton(String beanName) {
		return this.singletonObjects.get(beanName);
	}

	protected void addSingleton(String beanName, Object bean) {
		this.singletonObjects.put(beanName, bean);
	}

	public void registerDisposableBean(String beanName, DisposableBean bean) {
		disposableBeans.put(beanName, bean);
	}

	public void destroySingletons() {
		ArrayList<String> beanNames = new ArrayList<>(disposableBeans.keySet());
		for (String beanName : beanNames) {
			DisposableBean disposableBean = disposableBeans.remove(beanName);
			try {
				disposableBean.destroy();
			} catch (Exception e) {
				throw new BeansException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
			}
		}
	}
}
