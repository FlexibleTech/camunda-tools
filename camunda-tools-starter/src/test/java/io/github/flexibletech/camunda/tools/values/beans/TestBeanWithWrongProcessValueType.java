package io.github.flexibletech.camunda.tools.values.beans;

import io.github.flexibletech.camunda.tools.delegate.Delegate;
import io.github.flexibletech.camunda.tools.process.values.ProcessKeyValue;
import io.github.flexibletech.camunda.tools.process.values.ProcessValue;
import io.github.flexibletech.camunda.tools.values.TestDataFactory;
import io.github.flexibletech.camunda.tools.values.TestOutputObject;
import io.github.flexibletech.camunda.tools.values.TestValues;

public class TestBeanWithWrongProcessValueType {

    @Delegate(beanName = TestValues.TEST_DELEGATE_FIRST_NAME, key = TestValues.PROCESS_KEY)
    public TestOutputObject doAction(@ProcessKeyValue String processKey,
                                     @ProcessValue(value = TestValues.TEST_STRING_PROCESS_VALUE, type = Integer.class) Integer variable) {
        return TestDataFactory.newTestOutputObject();
    }

}
