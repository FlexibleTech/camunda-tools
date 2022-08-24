package io.github.flexibletech.camunda.tools.process.start;

import io.github.flexibletech.camunda.tools.common.ExpressionExecutor;
import io.github.flexibletech.camunda.tools.process.variables.ProcessVariable;
import io.github.flexibletech.camunda.tools.process.variables.ProcessVariablesCollector;
import org.apache.commons.lang3.ArrayUtils;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class ProcessStarter {
    private final RuntimeService runtimeService;
    private final GenericApplicationContext applicationContext;

    private final Logger log = LoggerFactory.getLogger(ProcessStarter.class);

    @Autowired
    public ProcessStarter(RuntimeService runtimeService, GenericApplicationContext applicationContext) {
        this.runtimeService = runtimeService;
        this.applicationContext = applicationContext;
    }

    void startProcess(StartProcess startProcessAnnotation, Object result) {
        String processKeyValue = applicationContext.getBeanFactory()
                .resolveEmbeddedValue(Objects.requireNonNull(startProcessAnnotation).processKey());
        ProcessVariable[] processVariables = startProcessAnnotation.variables();

        String businessKeyValue = (String) ExpressionExecutor.parseAndExecuteExpression(result, startProcessAnnotation.businessKeyValue());

        Map<String, Object> variableValues = new HashMap<>();

        if (ArrayUtils.isNotEmpty(processVariables)) {
            Map<String, String> variables = Arrays.stream(processVariables).collect(ProcessVariablesCollector.toValuesMap());
            variableValues = ExpressionExecutor.parseAndExecuteExpressions(result, variables);
        }

        runtimeService.createProcessInstanceByKey(processKeyValue)
                .businessKey(businessKeyValue)
                .setVariables(variableValues)
                .setVariable(startProcessAnnotation.businessKeyName(), businessKeyValue)
                .execute();

        log.info("Process with key {} has been started", processKeyValue);
    }

}
