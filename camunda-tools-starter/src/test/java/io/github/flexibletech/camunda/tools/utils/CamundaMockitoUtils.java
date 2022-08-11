package io.github.flexibletech.camunda.tools.utils;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class CamundaMockitoUtils {
    private CamundaMockitoUtils() {
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
