package com.avezah.springframework.test;

import com.avezah.springframework.context.support.ClassPathXmlApplicationContext;
import com.avezah.springframework.test.bean.Car;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyPlaceHolderTest {

    @Test
    public void testPropertyPlaceHolder() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:property-placeholder-configurer.xml");
        Car car = context.getBean("car", Car.class);
        assertThat(car.getBrand()).isEqualTo("lamborghini");
    }
}
