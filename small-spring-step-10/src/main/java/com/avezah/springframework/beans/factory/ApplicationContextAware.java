package com.avezah.springframework.beans.factory;

import com.avezah.springframework.beans.BeansException;
import com.avezah.springframework.context.ApplicationContext;

public interface ApplicationContextAware extends Aware{

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
