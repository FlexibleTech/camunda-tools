package io.github.flexibletech.camunda.tools.utils;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.MessageCorrelationBuilder;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.Map;

public class CamundaMockitoUtils {
    private CamundaMockitoUtils() {
    }

    public static void mockMessageCorrelation(RuntimeService runtimeService,
                                              ArgumentCaptor<String> businessKeyCaptor,
                                              ArgumentCaptor<String> messageCorrelationCaptor,
                                              ArgumentCaptor<Map<String, Object>> variablesCaptor) {
        var messageCorrelationBuilder = Mockito.mock(MessageCorrelationBuilder.class);
        Mockito.when(messageCorrelationBuilder.processInstanceBusinessKey(businessKeyCaptor.capture()))
                .thenReturn(messageCorrelationBuilder);
        Mockito.when(messageCorrelationBuilder.setVariables(variablesCaptor.capture()))
                .thenReturn(messageCorrelationBuilder);
        Mockito.doNothing().when(messageCorrelationBuilder).correlate();
        Mockito.when(runtimeService.createMessageCorrelation(messageCorrelationCaptor.capture()))
                .thenReturn(messageCorrelationBuilder);
    }

    public static void mockFindTask(TaskService taskService, String userTaskId) {
        var taskQuery = Mockito.mock(TaskQuery.class);
        var task = Mockito.mock(Task.class);
        Mockito.when(task.getId()).thenReturn(userTaskId);
        Mockito.when(taskQuery.processInstanceBusinessKey(ArgumentMatchers.anyString()))
                .thenReturn(taskQuery);
        Mockito.when(taskQuery.taskDefinitionKey(ArgumentMatchers.anyString()))
                .thenReturn(taskQuery);
        Mockito.when(taskQuery.singleResult()).thenReturn(task);
        Mockito.when(taskService.createTaskQuery()).thenReturn(taskQuery);
    }

    public static void mockFindNullableTask(TaskService taskService) {
        var taskQuery = Mockito.mock(TaskQuery.class);
        Mockito.when(taskQuery.processInstanceBusinessKey(ArgumentMatchers.anyString()))
                .thenReturn(taskQuery);
        Mockito.when(taskQuery.taskDefinitionKey(ArgumentMatchers.anyString()))
                .thenReturn(taskQuery);
        Mockito.when(taskQuery.singleResult()).thenReturn(null);
        Mockito.when(taskService.createTaskQuery()).thenReturn(taskQuery);
    }
}
