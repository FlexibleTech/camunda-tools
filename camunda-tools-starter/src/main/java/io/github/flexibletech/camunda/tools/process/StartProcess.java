package io.github.flexibletech.camunda.tools.process;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface StartProcess {
    String businessKeyName();

    String businessKeyValue();

    String processKey();

    ProcessVariable[] variables() default {};
}