package com.avezah.springframework.test;

import com.avezah.springframework.context.support.ClassPathXmlApplicationContext;
import com.avezah.springframework.test.bean.Car;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class FactoryBeanTest {

    @Test
    public void testFactoryBean() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:factory-bean.xml");
        Car car = applicationContext.getBean("car", Car.class);
        assertThat(car.getBrand()).isEqualTo("porsche");
    }
}
