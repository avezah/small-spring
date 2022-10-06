package com.avezah.springframework.test.bean;

public class UserService {

	String name;

	public UserService (String name) {
		this.name = name;
	}

	public void queryUserInfo(){
		System.out.println("查询用户信息：" + name);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("");
		sb.append(name);
		return sb.toString();
	}

}
