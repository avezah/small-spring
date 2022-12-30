package com.avezah.springframework.test.common.event;

import com.avezah.springframework.context.ApplicationListener;
import com.avezah.springframework.context.event.ContextRefreshedEvent;

public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("Context refreshed event: " + event);
    }
}
