package io.github.flexibletech.camunda.tools.delegate;

import io.github.flexibletech.camunda.tools.values.TestDataFactory;
import io.github.flexibletech.camunda.tools.values.TestOutputObject;
import io.github.flexibletech.camunda.tools.values.TestValues;
import org.apache.commons.collections4.MapUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.extension.mockito.CamundaMockito;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GenericDelegateTest {

    @Test
    public void shouldExecuteDelegateWithOutputVariables() throws Exception {
        DelegateExecution delegateExecution = CamundaMockito.delegateExecutionFake();
        var genericDelegate = TestDataFactory.newGenericDelegateWithVariables();

        genericDelegate.execute(delegateExecution);

        Assertions.assertEquals(delegateExecution.getVariable(TestValues.TEST_OUTPUT_OBJECT_VARIABLE_NAME),
                TestValues.TEST_OUTPUT_OBJECT_VARIABLE_RESULT
        );
    }

    @Test
    public void shouldExecuteDelegateWithoutOutputVariables() throws Exception {
        DelegateExecution delegateExecution = CamundaMockito.delegateExecutionFake();
        var genericDelegate = TestDataFactory.newGenericDelegateWithoutVariables();

        genericDelegate.execute(delegateExecution);

        Assertions.assertTrue(MapUtils.isEmpty(delegateExecution.getVariables()));
    }

    @Test
    public void shouldExecuteDelegateWithOutputVariableFunctionCall() throws Exception {
        DelegateExecution delegateExecution = CamundaMockito.delegateExecutionFake();
        var genericDelegate = TestDataFactory.newGenericDelegateWithoutVariableFunctionCall();

        genericDelegate.execute(delegateExecution);

        Assertions.assertEquals(delegateExecution.getVariable(TestValues.CLASS_NAME_VARIABLE),
                TestOutputObject.class.getName()
        );
    }

}
