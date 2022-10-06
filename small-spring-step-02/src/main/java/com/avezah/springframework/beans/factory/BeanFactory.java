package com.avezah.springframework.beans.factory;

import com.avezah.springframework.beans.BeansException;

public interface BeanFactory {

	Object getBean(String beanName) throws BeansException;
}
