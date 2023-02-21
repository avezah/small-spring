package com.avezah.springframework.aop;

public class TargetSource {

    private final Object target;

    public TargetSource(Object target) {
        this.target = target;
    }

    public Class<?>[] getTargetClass() {
        return this.getTarget().getClass().getInterfaces();
    }

    public Object getTarget() {
        return target;
    }
}
