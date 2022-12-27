package com.avezah.springframework.test.bean;

import com.avezah.springframework.beans.BeansException;
import com.avezah.springframework.beans.factory.ApplicationContextAware;
import com.avezah.springframework.beans.factory.BeanClassLoaderAware;
import com.avezah.springframework.beans.factory.BeanFactory;
import com.avezah.springframework.beans.factory.BeanFactoryAware;
import com.avezah.springframework.beans.factory.BeanNameAware;
import com.avezah.springframework.beans.factory.DisposableBean;
import com.avezah.springframework.beans.factory.InitializingBean;
import com.avezah.springframework.context.ApplicationContext;

public class UserService implements InitializingBean, DisposableBean, BeanNameAware, BeanClassLoaderAware, BeanFactoryAware, ApplicationContextAware {

	private String uId;

	private UserDao userDao;

	public String queryUserInfo(){
		return userDao.queryUserName(uId);
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("执行：UserService#destroy");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("执行：UserService#afterPropertiesSet");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println("ApplicationContext is: " + applicationContext);
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		System.out.println("ClassLoader is: " + classLoader);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("BeanFactory is: " + beanFactory);
	}

	@Override
	public void setBeanName(String name) {
		System.out.println("Bean name is: " + name);
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
