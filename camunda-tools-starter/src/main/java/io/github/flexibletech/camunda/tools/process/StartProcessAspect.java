package io.github.flexibletech.camunda.tools.process;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class StartProcessAspect {
    private final ProcessStarter processStarter;

    public StartProcessAspect(ProcessStarter processStarter) {
        this.processStarter = processStarter;
    }

    @AfterReturning(pointcut = "@annotation(io.github.flexibletech.camunda.tools.process.StartProcess)", returning = "result")
    public void execute(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        StartProcess startProcessAnnotation = AnnotationUtils.findAnnotation(method, StartProcess.class);

        processStarter.startProcess(startProcessAnnotation, result);
    }

}
