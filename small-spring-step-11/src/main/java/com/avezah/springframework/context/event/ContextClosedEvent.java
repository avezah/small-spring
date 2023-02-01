package com.avezah.springframework.context.event;

import com.avezah.springframework.context.ApplicationEvent;

public class ContextClosedEvent extends ApplicationEvent {
    public ContextClosedEvent(Object source) {
        super(source);
    }
}
