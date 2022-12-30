package com.avezah.springframework.test.common.event;

import com.avezah.springframework.context.ApplicationListener;

public class CustomEventListener implements ApplicationListener<CustomEvent> {
    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println("Custom event: " + event);
    }
}
