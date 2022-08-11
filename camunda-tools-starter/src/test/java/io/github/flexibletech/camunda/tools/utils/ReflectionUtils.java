package io.github.flexibletech.camunda.tools.utils;

import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static Method findMethod(String name, Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().equals(name)).findAny()
                .orElseThrow(() -> new IllegalAccessError("doAction method is not found"));
    }

    public static <A extends Annotation> A findAnnotationForMethod(Class<A> annotationType, String methodName, Class<?> targetClass) {
        Method method = findMethod(methodName, targetClass);

        return AnnotationUtils.findAnnotation(method, annotationType);
    }

}
