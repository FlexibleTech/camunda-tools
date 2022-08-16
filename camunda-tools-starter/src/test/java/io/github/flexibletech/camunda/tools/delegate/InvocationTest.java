package io.github.flexibletech.camunda.tools.delegate;

import io.github.flexibletech.camunda.tools.common.Constants;
import io.github.flexibletech.camunda.tools.utils.ReflectionUtils;
import io.github.flexibletech.camunda.tools.values.TestValues;
import io.github.flexibletech.camunda.tools.values.beans.TestBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

public class InvocationTest {

    @Test
    public void shouldExecuteActionWithArgument() throws InvocationTargetException, IllegalAccessException {
        Invocation invocation = Invocation.newInvocation(
                ReflectionUtils.findMethod("doActionFirst", TestBean.class),
                new TestBean(),
                new Object[]{Constants.BUSINESS_KEY_VALUE, TestValues.TEST_BEAN_DO_ACTION_ARG}
        );

        var result = invocation.execute(TestValues.PROCESS_KEY);

        Assertions.assertEquals(result, TestValues.TEST_BEAN_DO_ACTION_RESULT);
    }

    @Test
    public void shouldDontCreateInvocationWithNullableBeanAndDelegateMethod() {
        Assertions.assertThrows(AssertionError.class, () -> Invocation.newInvocation(null, null, null));
    }

    @Test
    public void shouldDontCreateInvocationWithNullableArgs() {
        Assertions.assertThrows(AssertionError.class, () -> Invocation.newInvocation(
                ReflectionUtils.findMethod("doActionFirst", TestBean.class),
                new TestBean(),
                null)
        );
    }

    @Test
    public void shouldDontCreateInvocationWithEmptyArgs() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Invocation.newInvocation(
                ReflectionUtils.findMethod("doActionFirst", TestBean.class),
                new TestBean(),
                new Object[]{})
        );
    }
}
