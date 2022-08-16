package io.github.flexibletech.camunda.tools.task.user;

import io.github.flexibletech.camunda.tools.utils.CamundaMockitoUtils;
import io.github.flexibletech.camunda.tools.utils.ReflectionUtils;
import io.github.flexibletech.camunda.tools.values.TestDataFactory;
import io.github.flexibletech.camunda.tools.values.TestValues;
import io.github.flexibletech.camunda.tools.values.beans.TestBean;
import org.camunda.bpm.engine.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class UserTaskExecutorTest {
    @Mock
    private TaskService taskService;

    @InjectMocks
    private UserTaskExecutor userTaskExecutor;
    @Captor
    private ArgumentCaptor<String> taskIdCaptor;
    @Captor
    private ArgumentCaptor<Map<String, Object>> variablesCaptor;

    @Test
    public void shouldExecuteUserTaskWithNotEmptyVariables() {
        CamundaMockitoUtils.mockFindTask(taskService, TestValues.USER_TASK_ID);
        Mockito.doNothing().when(taskService).setVariables(ArgumentMatchers.anyString(), variablesCaptor.capture());
        Mockito.doNothing().when(taskService).complete(taskIdCaptor.capture());

        Method userTaskMethod = ReflectionUtils.findMethod("doActionSeven", TestBean.class);

        userTaskExecutor.executeTask(
                TestDataFactory.newTestOutputObject(),
                userTaskMethod,
                new Object[]{TestValues.BUSINESS_KEY_VALUE}
        );

        Assertions.assertEquals(taskIdCaptor.getValue(), TestValues.USER_TASK_ID);
        Assertions.assertFalse(variablesCaptor.getValue().isEmpty());
    }

    @Test
    public void shouldExecuteUserTaskWithEmptyVariables() {
        CamundaMockitoUtils.mockFindTask(taskService, TestValues.USER_TASK_ID);
        Mockito.doNothing().when(taskService).complete(taskIdCaptor.capture());

        Method userTaskMethod = ReflectionUtils.findMethod("doActionEight", TestBean.class);

        userTaskExecutor.executeTask(
                TestDataFactory.newTestOutputObject(),
                userTaskMethod,
                new Object[]{TestValues.BUSINESS_KEY_VALUE}
        );

        Assertions.assertEquals(taskIdCaptor.getValue(), TestValues.USER_TASK_ID);
        Mockito.verify(taskService, Mockito.times(0))
                .setVariables(ArgumentMatchers.anyString(), ArgumentMatchers.anyMap());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForNullableTask() {
        CamundaMockitoUtils.mockFindNullableTask(taskService);

        Method userTaskMethod = ReflectionUtils.findMethod("doActionEight", TestBean.class);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                userTaskExecutor.executeTask(
                        TestDataFactory.newTestOutputObject(),
                        userTaskMethod,
                        new Object[]{TestValues.BUSINESS_KEY_VALUE}
                ));
    }
}
