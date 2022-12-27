package com.avezah.springframework.test.bean;

import java.util.HashMap;
import java.util.Map;

public class UserDao {
	private static Map<String, String> hashMap = new HashMap<>();

	public void initDataMethod() {
		System.out.println("执行：UserDao#initDataMethod");
		hashMap.put("10001", "张三");
		hashMap.put("10002", "李四");
		hashMap.put("10003", "王五");
	}

	public void destroyDataMethod() {
		System.out.println("执行：UserDao#destroyDataMethod");
		hashMap.clear();
	}

	public String queryUserName(String uId) {
		return hashMap.get(uId);
	}

}
