package com.avezah.springframework.beans.factory;

public interface InitializingBean {

    void afterPropertiesSet() throws Exception;

}
