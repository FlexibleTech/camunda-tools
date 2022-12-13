package io.github.flexibletech.camunda.tools.utils;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static Method findMethod(String name, Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().equals(name)).findAny()
                .orElseThrow(() -> new IllegalAccessError(String.format("%s method is not found", name)));
    }
}
