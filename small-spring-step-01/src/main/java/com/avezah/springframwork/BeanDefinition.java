package com.avezah.springframwork;


/**
 * 用于存放bean的实例信息
 */
public class BeanDefinition {

	private Object bean;

	public BeanDefinition(Object bean) {
		this.bean = bean;
	}

	public Object getBean() {
		return this.bean;
	}
}
