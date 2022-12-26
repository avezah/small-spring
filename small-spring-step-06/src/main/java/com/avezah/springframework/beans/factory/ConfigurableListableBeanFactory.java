package com.avezah.springframework.beans.factory;

import com.avezah.springframework.beans.BeansException;
import com.avezah.springframework.beans.factory.config.AutowireCapableBeanFactory;
import com.avezah.springframework.beans.factory.config.BeanDefinition;
import com.avezah.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.beans.Beans;

public interface ConfigurableListableBeanFactory extends AutowireCapableBeanFactory, ConfigurableBeanFactory, ListableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void preInstantiateSingletons() throws BeansException;
}
