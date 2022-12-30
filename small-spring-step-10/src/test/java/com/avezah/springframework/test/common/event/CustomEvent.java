package com.avezah.springframework.test.common.event;

import com.avezah.springframework.context.ApplicationContext;
import com.avezah.springframework.context.event.ApplicationContextEvent;

public class CustomEvent extends ApplicationContextEvent {
    public CustomEvent(ApplicationContext source) {
        super(source);
    }
}
