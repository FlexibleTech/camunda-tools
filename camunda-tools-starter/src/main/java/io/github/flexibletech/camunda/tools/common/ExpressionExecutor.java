package io.github.flexibletech.camunda.tools.common;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ExpressionExecutor {
    private static final ExpressionParser expressionParser = new SpelExpressionParser();

    private ExpressionExecutor() {
    }

    public static Object parseAndExecuteExpression(Object object, String stringExpression) {
        EvaluationContext context = new StandardEvaluationContext(object);
        Expression expression = expressionParser.parseExpression(stringExpression);

        return expression.getValue(context);
    }

    public static Map<String, Object> parseAndExecuteExpressions(Object object, Map<String, String> expressionMap) {
        Map<String, Object> executedValues = new HashMap<>();

        if (Objects.nonNull(expressionMap)) {
            expressionMap.forEach(
                    (variableName, stringExpression) ->
                            executedValues.put(variableName, parseAndExecuteExpression(object, stringExpression))
            );
        }

        return executedValues;
    }

}
