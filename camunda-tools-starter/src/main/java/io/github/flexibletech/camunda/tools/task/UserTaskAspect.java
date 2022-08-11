package io.github.flexibletech.camunda.tools.task;

import io.github.flexibletech.camunda.tools.common.AopUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserTaskAspect {
    private final UserTaskExecutor userTaskExecutor;

    public UserTaskAspect(UserTaskExecutor userTaskExecutor) {
        this.userTaskExecutor = userTaskExecutor;
    }

    @AfterReturning(pointcut = "@annotation(io.github.flexibletech.camunda.tools.task.UserTask)", returning = "result")
    public void execute(JoinPoint joinPoint, Object result) {
        userTaskExecutor.executeTask(result, AopUtils.findMethod(joinPoint), joinPoint.getArgs());
    }

}
