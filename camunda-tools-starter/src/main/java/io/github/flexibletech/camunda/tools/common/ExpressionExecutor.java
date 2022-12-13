package io.github.flexibletech.camunda.tools.common;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Spring spel executor.
 */
public class ExpressionExecutor {
    private static final ExpressionParser expressionParser = new SpelExpressionParser();

    private ExpressionExecutor() {
    }

    public static Object parseAndExecuteExpression(Object object, String stringExpression) {
        var context = new StandardEvaluationContext(object);
        var expression = expressionParser.parseExpression(stringExpression);

        return expression.getValue(context);
    }

    public static Map<String, Object> parseAndExecuteExpressions(Object object, Map<String, String> expressionMap) {
        return Optional.ofNullable(expressionMap)
                .stream()
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> parseAndExecuteExpression(object, entry.getValue())));
    }

}
