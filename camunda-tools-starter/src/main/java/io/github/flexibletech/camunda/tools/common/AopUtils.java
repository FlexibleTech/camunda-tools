package io.github.flexibletech.camunda.tools.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AopUtils {
    private AopUtils() {
    }

    public static <A extends Annotation> A findAnnotation(JoinPoint joinPoint, Class<A> clazz) {
        var method = findMethod(joinPoint);
        return AnnotationUtils.findAnnotation(method, clazz);
    }

    public static Method findMethod(JoinPoint joinPoint) {
        var signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
