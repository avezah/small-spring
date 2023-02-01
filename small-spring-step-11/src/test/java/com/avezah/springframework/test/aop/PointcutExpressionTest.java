package com.avezah.springframework.test.aop;

import com.avezah.springframework.aop.aspectj.AspectJExpressionPointCut;
import com.avezah.springframework.test.service.Sender;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.lang.reflect.Method;

public class PointcutExpressionTest {

    @Test
    public void testPointcutExpression() throws Exception {
        AspectJExpressionPointCut pointCut = new AspectJExpressionPointCut("execution(* com.avezah.springframework.test.service.Sender.*(..))");
        Class<Sender> clazz = Sender.class;
        Method method = clazz.getDeclaredMethod("send");

        Assertions.assertThat(pointCut.matches(clazz)).isTrue();
        Assertions.assertThat(pointCut.matches(method, clazz)).isTrue();
    }
}
