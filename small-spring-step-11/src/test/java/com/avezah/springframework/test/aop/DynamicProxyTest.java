package com.avezah.springframework.test.aop;

import com.avezah.springframework.aop.AdvisedSupport;
import com.avezah.springframework.aop.MethodMatcher;
import com.avezah.springframework.aop.TargetSource;
import com.avezah.springframework.aop.aspectj.AspectJExpressionPointCut;
import com.avezah.springframework.aop.framework.Cglib2AopProxy;
import com.avezah.springframework.aop.framework.JdkDynamicAopProxy;
import com.avezah.springframework.test.common.SenderInterceptor;
import com.avezah.springframework.test.service.Sender;
import com.avezah.springframework.test.service.SmsSender;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class DynamicProxyTest {

    private AdvisedSupport advisedSupport;

    @Before
    public void setup() {
        Sender sender = new SmsSender();

        advisedSupport = new AdvisedSupport();
        TargetSource targetSource = new TargetSource(sender);
        SenderInterceptor methodInterceptor = new SenderInterceptor();
        MethodMatcher methodMatcher = new AspectJExpressionPointCut("execution(* com.avezah.springframework.test.service.Sender.*(..))");
        advisedSupport.setTargetSource(targetSource);
        advisedSupport.setMethodInterceptor(methodInterceptor);
        advisedSupport.setMethodMatcher(methodMatcher);
    }

    @Test
    public void testJdkDynamicProxy() {
        Sender senderProxy = (Sender) new JdkDynamicAopProxy(advisedSupport).getProxy();
        Assertions.assertThat(senderProxy.send()).isTrue();

    }

    @Test
    public void testCglibDynamicProxy() throws Exception {
        Sender senderProxy = (Sender) new Cglib2AopProxy(advisedSupport).getProxy();
        Assertions.assertThat(senderProxy.send()).isTrue();
    }
}
