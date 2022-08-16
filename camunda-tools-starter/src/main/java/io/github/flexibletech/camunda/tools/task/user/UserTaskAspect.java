package io.github.flexibletech.camunda.tools.task.user;

import io.github.flexibletech.camunda.tools.task.TaskAspect;
import io.github.flexibletech.camunda.tools.task.TaskExecutor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserTaskAspect extends TaskAspect {

    protected UserTaskAspect(@Qualifier("userTaskExecutor") TaskExecutor taskExecutor) {
        super(taskExecutor);
    }

    @AfterReturning(pointcut = "@annotation(io.github.flexibletech.camunda.tools.task.user.UserTask)", returning = "result")
    public void execute(JoinPoint joinPoint, Object result) {
        super.execute(joinPoint, result);
    }

}
