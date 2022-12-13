package io.github.flexibletech.camunda.tools.delegate;

import io.github.flexibletech.camunda.tools.process.variables.ProcessVariable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a delegate bean will be registered for this method in the context of the spring.
 * The {@link TemplateDelegate} will be used as the delegate template.
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Delegate {
    /**
     * Represents the name of the delegate for which the bean will
     * be registered in the context of the spring.
     *
     * @return Delegate bean name
     */
    String beanName();

    /**
     * Represents the name of the key with which the process was launched in the camunda.
     *
     * @return The name of the camunda process.
     */
    String key();

    /**
     * Represents process variables. Optional parameter.
     *
     * @return Process variables.
     */
    ProcessVariable[] variables() default {};

    /**
     * If the throwBpmnError flag is set to true, then all exceptions will be wrapped
     * in a {@link org.camunda.bpm.engine.delegate.BpmnError}.
     *
     * @return true or false
     */
    boolean throwBpmnError() default false;
}
