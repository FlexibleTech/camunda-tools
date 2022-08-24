package io.github.flexibletech.camunda.tools.values.beans;

import io.github.flexibletech.camunda.tools.delegate.Delegate;
import io.github.flexibletech.camunda.tools.process.values.ProcessKeyValue;
import io.github.flexibletech.camunda.tools.process.variables.ProcessVariable;
import io.github.flexibletech.camunda.tools.values.TestDataFactory;
import io.github.flexibletech.camunda.tools.values.TestOutputObject;
import io.github.flexibletech.camunda.tools.values.TestValues;

public class TestBeanWithOutputVariableFunctionCall {

    @Delegate(beanName = TestValues.TEST_DELEGATE_FIRST_NAME, key = TestValues.PROCESS_KEY,
            variables =
                    {@ProcessVariable(name = TestValues.CLASS_NAME_VARIABLE, value = TestValues.CLASS_NAME_VARIABLE_EXPRESSION)})
    public TestOutputObject doAction(@ProcessKeyValue String processKey) {
        return TestDataFactory.newTestOutputObject();
    }
}
