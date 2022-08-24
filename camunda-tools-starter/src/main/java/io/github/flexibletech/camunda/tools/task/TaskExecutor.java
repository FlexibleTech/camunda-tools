package io.github.flexibletech.camunda.tools.task;

import io.github.flexibletech.camunda.tools.common.ExpressionExecutor;
import io.github.flexibletech.camunda.tools.process.values.ProcessKeyValue;
import io.github.flexibletech.camunda.tools.process.variables.ProcessVariable;
import io.github.flexibletech.camunda.tools.process.variables.ProcessVariablesCollector;
import org.apache.commons.collections4.MapUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class TaskExecutor {

    public abstract void executeTask(Object result, Method method, Object[] args);

    protected Map<String, Object> defineProcessVariables(ProcessVariable[] processVariables, Object result) {
        Map<String, String> variables = Arrays.stream(processVariables)
                .collect(ProcessVariablesCollector.toValuesMap());
        if (MapUtils.isNotEmpty(variables) && Objects.nonNull(result))
            return ExpressionExecutor.parseAndExecuteExpressions(result, variables);
        return null;
    }

    protected String findBusinessKeyValue(Method method, Object[] args) {
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
                        )
        );
    }

}
