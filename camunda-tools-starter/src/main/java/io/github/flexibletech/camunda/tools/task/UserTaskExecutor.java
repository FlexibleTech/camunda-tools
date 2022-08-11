package io.github.flexibletech.camunda.tools.task;

import io.github.flexibletech.camunda.tools.common.ExpressionExecutor;
import io.github.flexibletech.camunda.tools.process.ProcessKeyValue;
import io.github.flexibletech.camunda.tools.process.ProcessVariable;
import io.github.flexibletech.camunda.tools.process.ProcessVariablesCollector;
import org.apache.commons.collections4.MapUtils;
import org.camunda.bpm.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class UserTaskExecutor {
    private final TaskService taskService;

    private final Logger log = LoggerFactory.getLogger(UserTaskExecutor.class);

    public UserTaskExecutor(TaskService taskService) {
        this.taskService = taskService;
    }

    public void executeTask(Object result, Method method, Object[] args) {
        String businessKey = findBusinessKeyValue(method, args);
        UserTask taskAnnotation = AnnotationUtils.findAnnotation(method, UserTask.class);

        var task = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .taskDefinitionKey(Objects.requireNonNull(taskAnnotation).definitionKey())
                .singleResult();

        if (Objects.isNull(task))
            throw new IllegalArgumentException(
                    String.format("Task for businessKey %s and definitionKey %s is not found ",
                            businessKey,
                            taskAnnotation.definitionKey())
            );

        initProcessVariables(taskAnnotation.variables(), result, task.getId());

        taskService.complete(task.getId());
        log.info("Task with id {} has been completed", task.getId());
    }

    private void initProcessVariables(ProcessVariable[] processVariables, Object result, String taskId) {
        Map<String, String> variables = Arrays.stream(processVariables)
                .collect(ProcessVariablesCollector.toValuesMap());

        if (MapUtils.isNotEmpty(variables) && Objects.nonNull(result)) {
            var variableValues = ExpressionExecutor.parseAndExecuteExpressions(result, variables);
            taskService.setVariables(taskId, variableValues);
        }
    }

    private String findBusinessKeyValue(Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        return String.valueOf(
                IntStream.range(0, parameters.length)
                        .boxed()
                        .collect(Collectors.toMap(i -> parameters[i], i -> args[i]))
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getKey().isAnnotationPresent(ProcessKeyValue.class))
                        .map(Map.Entry::getValue)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException(
                                String.format("Missing ProcessKeyValue for user task method %s", method.getName()))
                        ));
    }

}
