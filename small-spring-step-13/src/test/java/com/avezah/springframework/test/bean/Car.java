package com.avezah.springframework.test.bean;

import com.avezah.springframework.stereotype.Component;

@Component
public class Car {

    private String brand;

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }
}
