package com.avezah.springframework.beans.factory;

import com.avezah.springframework.beans.BeansException;
import com.avezah.springframework.beans.factory.config.AutowireCapableBeanFactory;
import com.avezah.springframework.beans.factory.config.BeanDefinition;

public interface ConfigurableListableBeanFactory extends AutowireCapableBeanFactory, HierarchicalBeanFactory, ListableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;
}
