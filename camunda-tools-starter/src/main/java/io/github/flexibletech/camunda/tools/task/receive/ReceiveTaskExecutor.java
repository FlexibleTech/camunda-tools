package io.github.flexibletech.camunda.tools.task.receive;

import io.github.flexibletech.camunda.tools.task.TaskExecutor;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Component
public class ReceiveTaskExecutor extends TaskExecutor {
    private final RuntimeService runtimeService;

    private final Logger log = LoggerFactory.getLogger(ReceiveTaskExecutor.class);

    public ReceiveTaskExecutor(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Override
    public void executeTask(Object result, Method method, Object[] args) {
        var businessKey = findBusinessKeyValue(method, args);
        var taskAnnotation = AnnotationUtils.findAnnotation(method, ReceiveTask.class);

        var processVariables = defineProcessVariables(Objects.requireNonNull(taskAnnotation).variables(), result);

        runtimeService.createMessageCorrelation(taskAnnotation.definitionKey())
                .processInstanceBusinessKey(businessKey)
                .setVariables(processVariables)
                .correlate();

        log.info("Task with definition {} has been completed", taskAnnotation.definitionKey());
    }
}
