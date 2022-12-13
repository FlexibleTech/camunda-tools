package io.github.flexibletech.camunda.tools.process.start;

import io.github.flexibletech.camunda.tools.common.ExpressionExecutor;
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

/**
 * Class designed to declaratively launch the camunda process. It works with the annotation
 * and the result of the work of the process start method.
 */
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

    /**
     * Starts the process with the parameters specified in the StartProcess annotation.
     *
     * @param startProcessAnnotation StartProcess annotation
     * @param result                 The result returned by the function from which the values of the process variables will be obtained.
     */
    public void startProcess(StartProcess startProcessAnnotation, Object result) {
        if (Objects.nonNull(result)) start(startProcessAnnotation, result);
        else log.debug("Process can't be started, nullable method's result");
    }

    private void start(StartProcess startProcessAnnotation, Object result) {
        var processKeyValue = applicationContext.getBeanFactory()
                .resolveEmbeddedValue(Objects.requireNonNull(startProcessAnnotation).processKey());

        var businessKeyValue = (String) ExpressionExecutor.parseAndExecuteExpression(result,
                startProcessAnnotation.businessKeyValue());

        Map<String, Object> variableValues = new HashMap<>();

        if (ArrayUtils.isNotEmpty(startProcessAnnotation.variables())) {
            var variables = Arrays.stream(startProcessAnnotation.variables())
                    .collect(ProcessVariablesCollector.toValuesMap());
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
