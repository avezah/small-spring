package com.avezah.springframework.test;

import com.avezah.springframework.beans.factory.config.BeanDefinition;
import com.avezah.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.avezah.springframework.test.bean.UserService;
import org.junit.Assert;
import org.junit.Test;

public class ApiTest {

	@Test
	public void test_BeanFactory(){
		// 1. 初始化BeanFactory
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
		// 2. 注册Bean
		beanFactory.registerBeanDefinition("userService", beanDefinition);
		// 3. 获取Bean（查找BeanDefinition，创建单例Bean）
		UserService userService = (UserService) beanFactory.getBean("userService", "avezah");
		System.out.println("userService:" + userService);
		userService.queryUserInfo();
	}
}
