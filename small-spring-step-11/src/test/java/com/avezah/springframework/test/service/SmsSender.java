package com.avezah.springframework.test.service;

public class SmsSender implements Sender{
    @Override
    public boolean send() {
        System.out.println("Send sms message.");
        return true;
    }
}
