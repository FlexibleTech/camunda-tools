package io.github.flexibletech.camunda.tools.task.user;

import io.github.flexibletech.camunda.tools.process.ProcessVariable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface UserTask {
    String definitionKey();

    ProcessVariable[] variables() default {};
}
