package io.github.flexibletech.camunda.tools.task;

import io.github.flexibletech.camunda.tools.common.AopUtils;
import org.aspectj.lang.JoinPoint;

public abstract class TaskAspect {
    private final TaskExecutor taskExecutor;

    protected TaskAspect(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void execute(JoinPoint joinPoint, Object result) {
        taskExecutor.executeTask(result, AopUtils.findMethod(joinPoint), joinPoint.getArgs());
    }
}
