package io.github.flexibletech.camunda.tools.process.start;

import io.github.flexibletech.camunda.tools.common.AopUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class StartProcessAspect {
    private final ProcessStarter processStarter;

    public StartProcessAspect(ProcessStarter processStarter) {
        this.processStarter = processStarter;
    }

    @AfterReturning(pointcut = "@annotation(io.github.flexibletech.camunda.tools.process.start.StartProcess)", returning = "result")
    public void execute(JoinPoint joinPoint, Object result) {
        StartProcess startProcessAnnotation = AopUtils.findAnnotation(joinPoint, StartProcess.class);
        processStarter.startProcess(startProcessAnnotation, result);
    }

}
