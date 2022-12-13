package io.github.flexibletech.camunda.tools.common;

import io.github.flexibletech.camunda.tools.values.TestValues;
import io.github.flexibletech.camunda.tools.values.beans.TestBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class ExpressionExecutorTest {

    @Test
    public void shouldParseAndExecuteExpression() {
        var testBean = new TestBean();

        var result = (String) ExpressionExecutor.parseAndExecuteExpression(testBean, "processKey()");

        Assertions.assertEquals(result, TestValues.PROCESS_KEY);
    }

    @Test
    public void shouldParseAndExecuteEmptyExpressions() {
        var testBean = new TestBean();

        var result = ExpressionExecutor.parseAndExecuteExpressions(testBean, null);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldParseAndExecuteExpressions() {
        var testBean = new TestBean();
        var variables = Map.of(TestValues.PROCESS_KEY, "processKey()");

        var result = ExpressionExecutor.parseAndExecuteExpressions(testBean, variables);

        Assertions.assertEquals(result.get(TestValues.PROCESS_KEY), TestValues.PROCESS_KEY);
    }
}
