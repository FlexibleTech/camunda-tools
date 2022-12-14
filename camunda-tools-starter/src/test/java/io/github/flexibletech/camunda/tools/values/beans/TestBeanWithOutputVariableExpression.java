package io.github.flexibletech.camunda.tools.values.beans;

import io.github.flexibletech.camunda.tools.delegate.Delegate;
import io.github.flexibletech.camunda.tools.process.values.ProcessKeyValue;
import io.github.flexibletech.camunda.tools.process.variables.ProcessVariable;
import io.github.flexibletech.camunda.tools.values.TestDataFactory;
import io.github.flexibletech.camunda.tools.values.TestOutputObject;
import io.github.flexibletech.camunda.tools.values.TestValues;

public class TestBeanWithOutputVariableExpression {

    @Delegate(beanName = TestValues.TEST_DELEGATE_FIRST_NAME, key = TestValues.PROCESS_KEY,
            variables = {@ProcessVariable(name = TestValues.TEST_OUTPUT_OBJECT_VARIABLE_NAME,
                            value = TestValues.TEST_OUTPUT_OBJECT_VARIABLE_VALUE)})
    public TestOutputObject doAction(@ProcessKeyValue String processKey) {
        return TestDataFactory.newTestOutputObject();
    }

}
