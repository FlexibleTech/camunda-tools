package io.github.flexibletech.camunda.tools.delegate;

import io.github.flexibletech.camunda.tools.common.Constants;
import io.github.flexibletech.camunda.tools.utils.ReflectionUtils;
import io.github.flexibletech.camunda.tools.values.TestValues;
import io.github.flexibletech.camunda.tools.values.beans.TestBean;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

public class InvocationTest {

    @Test
    public void shouldExecuteActionWithArgument() throws InvocationTargetException, IllegalAccessException {
        var invocation = Invocation.newInvocation(
                ReflectionUtils.findMethod("doActionFirst", TestBean.class),
                new TestBean(),
                new Object[]{Constants.BUSINESS_KEY_VALUE, TestValues.TEST_BEAN_DO_ACTION_ARG},
                false);

        var result = invocation.execute(TestValues.PROCESS_KEY);

        Assertions.assertEquals(result, TestValues.TEST_BEAN_DO_ACTION_RESULT);
    }

    @Test
    public void shouldDontCreateInvocationWithNullableBeanAndDelegateMethod() {
        Assertions.assertThrows(AssertionError.class, () -> Invocation.newInvocation(null, null, null, false));
    }

    @Test
    public void shouldDontCreateInvocationWithNullableArgs() {
        Assertions.assertThrows(
                AssertionError.class,
                () -> Invocation.newInvocation(ReflectionUtils.findMethod("doActionFirst", TestBean.class),
                        new TestBean(),
                        null,
                        false));
    }

    @Test
    public void shouldDontCreateInvocationWithEmptyArgs() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Invocation.newInvocation(ReflectionUtils.findMethod("doActionFirst", TestBean.class),
                        new TestBean(),
                        new Object[]{},
                        false));
    }

    @Test
    public void shouldThrowBpmnError() {
        var invocation = Invocation.newInvocation(
                ReflectionUtils.findMethod("doActionEleven", TestBean.class),
                new TestBean(),
                new Object[]{Constants.BUSINESS_KEY_VALUE, TestValues.TEST_BEAN_DO_ACTION_ARG},
                true);

        Assertions.assertThrows(BpmnError.class, () -> invocation.execute(TestValues.PROCESS_KEY));
    }
}
