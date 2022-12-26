package com.avezah.springframework.test.common;

import com.avezah.springframework.beans.BeansException;
import com.avezah.springframework.beans.factory.config.BeanPostProcessor;
import com.avezah.springframework.test.bean.UserService;

public class CustomBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("userService".equals(beanName)) {
            UserService userService = (UserService) bean;
            userService.setuId("10003");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
