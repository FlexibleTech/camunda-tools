package io.github.flexibletech.camunda.tools.delegate;

import io.github.flexibletech.camunda.tools.values.TestDataFactory;
import io.github.flexibletech.camunda.tools.values.TestOutputObject;
import io.github.flexibletech.camunda.tools.values.TestValues;
import org.apache.commons.collections4.MapUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class GenericDelegateTest {

    @Captor
    private ArgumentCaptor<Map<String, Object>> captor;

    @Test
    public void shouldExecuteDelegateWithOutputVariables() throws Exception {
        var delegateExecution = Mockito.mock(DelegateExecution.class);
        Mockito.when(delegateExecution.getVariable(TestValues.BUSINESS_KEY_NAME)).thenReturn(TestValues.BUSINESS_KEY_VALUE);
        Mockito.doNothing().when(delegateExecution).setVariables(captor.capture());

        var genericDelegate = TestDataFactory.newGenericDelegateWithVariables();

        genericDelegate.execute(delegateExecution);

        var variables = captor.getAllValues().get(0);
        Assertions.assertEquals(variables.get(TestValues.TEST_OUTPUT_OBJECT_VARIABLE_NAME),
                TestValues.TEST_OUTPUT_OBJECT_VARIABLE_RESULT);
    }

    @Test
    public void shouldExecuteDelegateWithoutOutputVariables() throws Exception {
        var delegateExecution = Mockito.mock(DelegateExecution.class);
        Mockito.when(delegateExecution.getVariable(TestValues.BUSINESS_KEY_NAME)).thenReturn(TestValues.BUSINESS_KEY_VALUE);
        Mockito.doNothing().when(delegateExecution).setVariables(captor.capture());

        var genericDelegate = TestDataFactory.newGenericDelegateWithoutVariables();

        genericDelegate.execute(delegateExecution);

        var variables = captor.getAllValues().get(0);
        Assertions.assertTrue(MapUtils.isEmpty(variables));
    }

    @Test
    public void shouldExecuteDelegateWithOutputVariableFunctionCall() throws Exception {
        var delegateExecution = Mockito.mock(DelegateExecution.class);
        Mockito.when(delegateExecution.getVariable(TestValues.BUSINESS_KEY_NAME)).thenReturn(TestValues.BUSINESS_KEY_VALUE);
        Mockito.doNothing().when(delegateExecution).setVariables(captor.capture());

        var genericDelegate = TestDataFactory.newGenericDelegateWithoutVariableFunctionCall();

        genericDelegate.execute(delegateExecution);

        var variables = captor.getAllValues().get(0);
        Assertions.assertEquals(variables.get(TestValues.CLASS_NAME_VARIABLE), TestOutputObject.class.getName());
    }

}
