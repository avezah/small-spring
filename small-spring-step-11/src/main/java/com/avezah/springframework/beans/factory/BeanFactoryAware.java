package com.avezah.springframework.beans.factory;

import com.avezah.springframework.beans.BeansException;

public interface BeanFactoryAware extends Aware{

    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
