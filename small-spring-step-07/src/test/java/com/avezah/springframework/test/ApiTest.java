package com.avezah.springframework.test;

import com.avezah.springframework.context.support.ClassPathXmlApplicationContext;
import com.avezah.springframework.test.bean.UserService;
import org.junit.Test;

public class ApiTest {

    @Test
    public void test() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();

        UserService userService = applicationContext.getBean("userService", UserService.class);
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);
    }
}
