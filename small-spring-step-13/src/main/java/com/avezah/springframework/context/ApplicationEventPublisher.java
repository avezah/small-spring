package com.avezah.springframework.context;

public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);
}
