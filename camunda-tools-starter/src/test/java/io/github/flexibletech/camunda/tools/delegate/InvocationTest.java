package io.github.flexibletech.camunda.tools.delegate;

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
                new Object[]{TestValues.TEST_BEAN_DO_ACTION_ARG}
        );

        var result = invocation.execute(TestValues.PROCESS_KEY);

        Assertions.assertEquals(result, TestValues.TEST_BEAN_DO_ACTION_RESULT);
    }

    @Test
    public void shouldExecuteActionWithoutArgument() throws InvocationTargetException, IllegalAccessException {
        Invocation invocation = Invocation.newInvocation(
                ReflectionUtils.findMethod("doActionSecond", TestBean.class),
                new TestBean(),
                new Object[]{}
        );

        var result = invocation.execute(TestValues.PROCESS_KEY);

        Assertions.assertEquals(result, TestValues.PROCESS_KEY);
    }

    @Test
    public void shouldDontCreateInvocationWithNullableBeanAndAction() {
        Assertions.assertThrows(AssertionError.class, () -> Invocation.newInvocation(null, null, null));
    }

}
