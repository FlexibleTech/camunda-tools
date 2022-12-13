package io.github.flexibletech.camunda.tools.delegate;

import io.github.flexibletech.camunda.tools.common.ExpressionExecutor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.Map;

/**
 * Represents a template for creating camunda delegate beans in the context of a spring.
 * The TemplateDelegate works according to the following principle:
 *  1) The processKey variable is retrieved from the DelegateExecution,
 *     which is the key with which the process was launched in the Camunda;
 *  2) The method for which the delegate was created is executed;
 *  3) The result of the method execution is used to obtain and initialize process variables.
 */
public class TemplateDelegate implements JavaDelegate {
    private Invocation invocation;
    private Map<String, String> expressionVariables;
    private String processKey;

    public TemplateDelegate() {
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        var processKeyValue = (String) execution.getVariable(processKey);
        var result = invocation.execute(processKeyValue);

        execution.setVariables(ExpressionExecutor.parseAndExecuteExpressions(result, expressionVariables));
    }

    public void setInvocation(Invocation invocation) {
        this.invocation = invocation;
    }

    public void setExpressionVariables(Map<String, String> expressionVariables) {
        this.expressionVariables = expressionVariables;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }
}
