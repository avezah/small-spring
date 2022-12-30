package com.avezah.springframework.test.common.event;

import com.avezah.springframework.context.ApplicationListener;
import com.avezah.springframework.context.event.ContextClosedEvent;

public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("Context closed event: " + event);
    }
}
