package io.github.flexibletech.camunda.tools.task.user;

import io.github.flexibletech.camunda.tools.task.TaskExecutor;
import org.camunda.bpm.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Component
public class UserTaskExecutor extends TaskExecutor {
    private final TaskService taskService;

    private final Logger log = LoggerFactory.getLogger(UserTaskExecutor.class);

    protected UserTaskExecutor(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void executeTask(Object result, Method method, Object[] args) {
        var businessKey = findBusinessKeyValue(method, args);
        var taskAnnotation = AnnotationUtils.findAnnotation(method, UserTask.class);

        var task = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .taskDefinitionKey(Objects.requireNonNull(taskAnnotation).definitionKey())
                .singleResult();

        if (Objects.isNull(task))
            throw new IllegalArgumentException(
                    String.format("Task for businessKey %s and definitionKey %s is not found ",
                            businessKey, taskAnnotation.definitionKey())
            );

        var processVariables = defineProcessVariables(taskAnnotation.variables(), result);

        taskService.setVariables(task.getId(), processVariables);
        taskService.complete(task.getId());

        log.info("Task with definition {} has been completed", taskAnnotation.definitionKey());
    }
}
