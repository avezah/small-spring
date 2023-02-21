package com.avezah.springframework.test.aop;

import com.avezah.springframework.context.support.ClassPathXmlApplicationContext;
import com.avezah.springframework.test.service.Sender;
import org.junit.Test;

public class AutoProxyTest {
    @Test
    public void testAutoProxy() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:step-12-auto-proxy.xml");
        Sender sender = context.getBean("sender", Sender.class);
        sender.send();
    }
}
