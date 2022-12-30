package com.avezah.springframework.test;

import com.avezah.springframework.context.support.ClassPathXmlApplicationContext;
import com.avezah.springframework.test.common.event.CustomEvent;
import org.junit.Test;

public class EventAndEventListenerTest {

    @Test
    public void testEventListener() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:event-and-event-listener.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext));
        applicationContext.close();
    }
}
