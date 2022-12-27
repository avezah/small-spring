package com.avezah.springframework.test.bean;

import com.avezah.springframework.beans.factory.DisposableBean;
import com.avezah.springframework.beans.factory.InitializingBean;

public class UserService implements InitializingBean, DisposableBean {

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
