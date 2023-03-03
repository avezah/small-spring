package com.avezah.springframework.test;

import com.avezah.springframework.context.support.ClassPathXmlApplicationContext;
import com.avezah.springframework.test.bean.Car;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PackageScanTest {

    @Test
    public void testPackageScan() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:package-scan.xml");
        Car car = context.getBean("car", Car.class);
        assertThat(car).isNotNull();
    }
}
