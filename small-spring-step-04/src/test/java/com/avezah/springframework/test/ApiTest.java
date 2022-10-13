package com.avezah.springframework.test;

import com.avezah.springframework.beans.PropertyValue;
import com.avezah.springframework.beans.PropertyValues;
import com.avezah.springframework.beans.factory.config.BeanDefinition;
import com.avezah.springframework.beans.factory.config.BeanReference;
import com.avezah.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.avezah.springframework.test.bean.UserDao;
import com.avezah.springframework.test.bean.UserService;
import org.junit.Test;

public class ApiTest{

    @Test
    public void test_BeanFactory() {
        // 1. 初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2. 注册 userDao
        beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));
  
        // 3. 设置 userService 属性
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("uId", "10001"));
        propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));

        // 4. 注册 userService
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        // 5. 获取 userService
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
    }
}
