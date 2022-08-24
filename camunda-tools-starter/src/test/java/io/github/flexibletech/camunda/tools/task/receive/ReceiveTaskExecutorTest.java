package io.github.flexibletech.camunda.tools.task.receive;

import io.github.flexibletech.camunda.tools.utils.CamundaMockitoUtils;
import io.github.flexibletech.camunda.tools.utils.ReflectionUtils;
import io.github.flexibletech.camunda.tools.values.TestDataFactory;
import io.github.flexibletech.camunda.tools.values.TestValues;
import io.github.flexibletech.camunda.tools.values.beans.TestBean;
import org.apache.commons.collections4.MapUtils;
import org.camunda.bpm.engine.RuntimeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ReceiveTaskExecutorTest {
    @Mock
    private RuntimeService runtimeService;

    @InjectMocks
    private ReceiveTaskExecutor receiveTaskExecutor;

    @Captor
    private ArgumentCaptor<Map<String, Object>> processVariablesCaptor;

    @Captor
    private ArgumentCaptor<String> businessKeyCaptor;

    @Captor
    private ArgumentCaptor<String> messageCorrelationCaptor;

    @Test
    public void shouldExecuteReceiveTaskWithNotEmptyVariables() {
        CamundaMockitoUtils.mockMessageCorrelation(runtimeService, businessKeyCaptor,
                messageCorrelationCaptor, processVariablesCaptor);

        Method receiveTaskMethod = ReflectionUtils.findMethod("doActionNine", TestBean.class);

        receiveTaskExecutor.executeTask(TestDataFactory.newTestOutputObject(),
                receiveTaskMethod,
                new Object[]{TestValues.BUSINESS_KEY_VALUE});

        Assertions.assertEquals(businessKeyCaptor.getValue(), TestValues.BUSINESS_KEY_VALUE);
        Assertions.assertEquals(messageCorrelationCaptor.getValue(), TestValues.RECEIVE_TASK_FIRST);
        Assertions.assertTrue(MapUtils.isNotEmpty(processVariablesCaptor.getValue()));
    }

    @Test
    public void shouldExecuteReceiveTaskWithEmptyVariables() {
        CamundaMockitoUtils.mockMessageCorrelation(runtimeService, businessKeyCaptor,
                messageCorrelationCaptor, processVariablesCaptor);

        Method receiveTaskMethod = ReflectionUtils.findMethod("doActionTen", TestBean.class);

        receiveTaskExecutor.executeTask(TestDataFactory.newTestOutputObject(),
                receiveTaskMethod,
                new Object[]{TestValues.BUSINESS_KEY_VALUE});

        Assertions.assertEquals(businessKeyCaptor.getValue(), TestValues.BUSINESS_KEY_VALUE);
        Assertions.assertEquals(messageCorrelationCaptor.getValue(), TestValues.RECEIVE_TASK_FIRST);
        Assertions.assertTrue(MapUtils.isEmpty(processVariablesCaptor.getValue()));
    }

}
