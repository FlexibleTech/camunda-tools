package io.github.flexibletech.camunda.tools.task.receive;

import io.github.flexibletech.camunda.tools.task.TaskAspect;
import io.github.flexibletech.camunda.tools.task.TaskExecutor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ReceiveTaskAspect extends TaskAspect {

    protected ReceiveTaskAspect(@Qualifier("receiveTaskExecutor") TaskExecutor taskExecutor) {
        super(taskExecutor);
    }

    @AfterReturning(pointcut = "@annotation(io.github.flexibletech.camunda.tools.task.receive.ReceiveTask)", returning = "result")
    public void execute(JoinPoint joinPoint, Object result) {
        super.execute(joinPoint, result);
    }

}
