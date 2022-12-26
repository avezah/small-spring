package com.avezah.springframework.context;

import com.avezah.springframework.beans.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext{

    void refresh() throws BeansException;
}
