package com.avezah.springframework.context.event;

import com.avezah.springframework.context.ApplicationEvent;

public class ContextRefreshedEvent extends ApplicationEvent {
    public ContextRefreshedEvent(Object source) {
        super(source);
    }
}
