package io.github.flexibletech.camunda.tools.process.values;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.PARAMETER)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ProcessValue {
    String value();

    Class<?> type();

    String delegate() default StringUtils.EMPTY;
}
