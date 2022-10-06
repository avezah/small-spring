package com.avezah.springframwork.test;

import com.avezah.springframwork.BeanDefinition;
import com.avezah.springframwork.BeanFactory;
import com.avezah.springframwork.test.bean.UserService;
import org.junit.Test;

public class ApiTest {

	@Test
	public void test_BeanFactory() {
		// 初始化beanFactory
		BeanFactory beanFactory = new BeanFactory();

		// 注入bean
		BeanDefinition beanDefinition = new BeanDefinition(new UserService());
		beanFactory.registerBeanDefinition("userService", beanDefinition);

		// 获取bean
		UserService userService = (UserService) beanFactory.getBean("userService");
		userService.queryUserInfo();
	}
}
