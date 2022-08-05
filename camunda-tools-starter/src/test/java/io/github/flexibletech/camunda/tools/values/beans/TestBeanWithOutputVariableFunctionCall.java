package io.github.flexibletech.camunda.tools.values.beans;

import io.github.flexibletech.camunda.tools.delegate.Delegate;
import io.github.flexibletech.camunda.tools.process.ProcessKeyValue;
import io.github.flexibletech.camunda.tools.process.ProcessVariable;
import io.github.flexibletech.camunda.tools.values.TestOutputObject;
import io.github.flexibletech.camunda.tools.values.TestValues;

public class TestBeanWithOutputVariableFunctionCall {

    @Delegate(beanName = TestValues.TEST_DELEGATE_FIRST_NAME, key = TestValues.PROCESS_KEY,
            variables = {@ProcessVariable(
                    name = TestValues.CLASS_NAME_VARIABLE,
                    value = TestValues.CLASS_NAME_VARIABLE_EXPRESSION
            )})
    public TestOutputObject doAction(@ProcessKeyValue String processKey) {
        return new TestOutputObject(TestValues.TEST_OUTPUT_OBJECT_VARIABLE_RESULT);
    }
}
