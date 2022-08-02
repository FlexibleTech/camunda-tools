package io.github.flexibletech.camunda.tools.delegate;

import io.github.flexibletech.camunda.tools.common.ExpressionExecutor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.Map;

/**
 * A generic delegate from which the beans will be created.
 */
public class GenericDelegate implements JavaDelegate {
    private Invocation invocation;
    private Map<String, String> variables;
    private String processKeyName;

    public GenericDelegate() {
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String processKeyValue = (String) execution.getVariable(processKeyName);
        Object result = invocation.execute(processKeyValue);

        execution.setVariables(ExpressionExecutor.parseAndExecuteExpressions(result, variables));
    }

    public void setInvocation(Invocation invocation) {
        this.invocation = invocation;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    public void setProcessKeyName(String processKeyName) {
        this.processKeyName = processKeyName;
    }
}
