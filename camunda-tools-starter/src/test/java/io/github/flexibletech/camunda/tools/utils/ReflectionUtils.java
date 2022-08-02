package io.github.flexibletech.camunda.tools.utils;

import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static Method findMethod(String name, Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().equals(name)).findAny()
                .orElseThrow(() -> new IllegalAccessError("doAction method is not found"));
    }

    public static <A extends Annotation> List<A> findAnnotationsOfMethods(Class<A> annotationType, Object target) {
        Method[] methods = target.getClass().getMethods();
        List<A> annotations = new ArrayList<>();

        for (Method method : methods) {
            A annotation = AnnotationUtils.findAnnotation(method, annotationType);
            annotations.add(annotation);
        }

        return annotations;
    }

    public static <A extends Annotation> A findFirstAnnotationOfMethods(Class<A> annotationType, Object target) {
        return findAnnotationsOfMethods(annotationType, target).stream()
                .findFirst()
                .orElseThrow();
    }

}
