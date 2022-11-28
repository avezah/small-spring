package com.avezah.springframework.beans.factory.support;

import com.avezah.springframework.beans.BeansException;
import com.avezah.springframework.core.io.Resource;
import com.avezah.springframework.core.io.ResourceLoader;

public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource) throws BeansException;

    void loadBeanDefinitions(Resource... resources) throws BeansException;

    void loadBeanDefinitions(String location) throws BeansException;

    void loadBeanDefinitions(String... location) throws BeansException;
}
