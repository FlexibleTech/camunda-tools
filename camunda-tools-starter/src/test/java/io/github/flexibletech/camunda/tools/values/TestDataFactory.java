package io.github.flexibletech.camunda.tools.values;

import io.github.flexibletech.camunda.tools.delegate.GenericDelegate;
import io.github.flexibletech.camunda.tools.delegate.Invocation;
import io.github.flexibletech.camunda.tools.utils.ReflectionUtils;
import io.github.flexibletech.camunda.tools.values.beans.TestBeanWithOutputVariableExpression;
import io.github.flexibletech.camunda.tools.values.beans.TestBeanWithOutputVariableFunctionCall;

import java.util.Map;

public class TestDataFactory {
    private TestDataFactory() {
    }

    public static GenericDelegate newGenericDelegateWithVariables() {
        var genericDelegate = new GenericDelegate();

        genericDelegate.setInvocation(
                Invocation.newInvocation(
                        ReflectionUtils.findMethod("doAction", TestBeanWithOutputVariableExpression.class),
                        new TestBeanWithOutputVariableExpression(),
                        null
                )
        );
        genericDelegate.setProcessKeyName(TestValues.PROCESS_KEY);
        genericDelegate.setVariables(
                Map.of(
                        TestValues.TEST_OUTPUT_OBJECT_VARIABLE_NAME,
                        TestValues.TEST_OUTPUT_OBJECT_VARIABLE_VALUE
                )
        );
        return genericDelegate;
    }

    public static GenericDelegate newGenericDelegateWithoutVariables() {
        var genericDelegate = new GenericDelegate();

        genericDelegate.setInvocation(
                Invocation.newInvocation(
                        ReflectionUtils.findMethod("doAction", TestBeanWithOutputVariableExpression.class),
                        new TestBeanWithOutputVariableExpression(),
                        null
                )
        );
        genericDelegate.setProcessKeyName(TestValues.PROCESS_KEY);
        return genericDelegate;
    }

    public static GenericDelegate newGenericDelegateWithoutVariableFunctionCall() {
        var genericDelegate = new GenericDelegate();

        genericDelegate.setInvocation(
                Invocation.newInvocation(
                        ReflectionUtils.findMethod("doAction", TestBeanWithOutputVariableFunctionCall.class),
                        new TestBeanWithOutputVariableFunctionCall(),
                        null
                )
        );
        genericDelegate.setProcessKeyName(TestValues.PROCESS_KEY);
        genericDelegate.setVariables(
                Map.of(
                        TestValues.CLASS_NAME_VARIABLE,
                        TestValues.CLASS_NAME_VARIABLE_EXPRESSION
                )
        );
        return genericDelegate;
    }

}
