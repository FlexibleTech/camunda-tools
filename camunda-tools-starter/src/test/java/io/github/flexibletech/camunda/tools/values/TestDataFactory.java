package io.github.flexibletech.camunda.tools.values;

import io.github.flexibletech.camunda.tools.common.Constants;
import io.github.flexibletech.camunda.tools.delegate.TemplateDelegate;
import io.github.flexibletech.camunda.tools.delegate.Invocation;
import io.github.flexibletech.camunda.tools.utils.ReflectionUtils;
import io.github.flexibletech.camunda.tools.values.beans.TestBeanWithOutputVariableExpression;
import io.github.flexibletech.camunda.tools.values.beans.TestBeanWithOutputVariableFunctionCall;

import java.util.Map;

public class TestDataFactory {
    private TestDataFactory() {
    }

    public static TemplateDelegate newTemplateDelegateWithVariables() {
        var templateDelegate = new TemplateDelegate();

        templateDelegate.setInvocation(
                Invocation.newInvocation(ReflectionUtils.findMethod("doAction", TestBeanWithOutputVariableExpression.class),
                        new TestBeanWithOutputVariableExpression(),
                        new Object[]{Constants.BUSINESS_KEY_VALUE}));

        templateDelegate.setProcessKey(TestValues.BUSINESS_KEY_NAME);
        templateDelegate.setExpressionVariables(Map.of(TestValues.TEST_OUTPUT_OBJECT_VARIABLE_NAME,
                TestValues.TEST_OUTPUT_OBJECT_VARIABLE_VALUE));

        return templateDelegate;
    }

    public static TemplateDelegate newTemplateDelegateWithoutVariables() {
        var templateDelegate = new TemplateDelegate();

        templateDelegate.setInvocation(
                Invocation.newInvocation(ReflectionUtils.findMethod("doAction", TestBeanWithOutputVariableExpression.class),
                        new TestBeanWithOutputVariableExpression(),
                        new Object[]{Constants.BUSINESS_KEY_VALUE}));

        templateDelegate.setProcessKey(TestValues.BUSINESS_KEY_VALUE);
        return templateDelegate;
    }

    public static TemplateDelegate newTemplateDelegateWithoutVariableFunctionCall() {
        var templateDelegate = new TemplateDelegate();

        templateDelegate.setInvocation(
                Invocation.newInvocation(ReflectionUtils.findMethod("doAction", TestBeanWithOutputVariableFunctionCall.class),
                new TestBeanWithOutputVariableFunctionCall(),
                new Object[]{Constants.BUSINESS_KEY_VALUE}));

        templateDelegate.setProcessKey(TestValues.BUSINESS_KEY_VALUE);
        templateDelegate.setExpressionVariables(Map.of(TestValues.CLASS_NAME_VARIABLE, TestValues.CLASS_NAME_VARIABLE_EXPRESSION));
        return templateDelegate;
    }

    public static TestOutputObject newTestOutputObject() {
        return new TestOutputObject(TestValues.TEST_OUTPUT_OBJECT_VARIABLE_RESULT);
    }

}
