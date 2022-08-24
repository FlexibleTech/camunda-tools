package io.github.flexibletech.camunda.tools.values.beans;

import io.github.flexibletech.camunda.tools.delegate.Delegate;
import io.github.flexibletech.camunda.tools.delegate.Delegates;
import io.github.flexibletech.camunda.tools.process.values.ProcessKeyValue;
import io.github.flexibletech.camunda.tools.process.values.ProcessValue;
import io.github.flexibletech.camunda.tools.process.values.ProcessValues;
import io.github.flexibletech.camunda.tools.values.TestValues;

public class TestBeanWithMultipleDelegate {

    @Delegates(values = {
            @Delegate(beanName = TestValues.TEST_DELEGATE_FIRST_NAME, key = TestValues.PROCESS_KEY),
            @Delegate(beanName = TestValues.TEST_DELEGATE_SECOND_NAME, key = TestValues.PROCESS_KEY)})
    public String doAction(@ProcessKeyValue String processKey,
                           @ProcessValues(values = {
                                   @ProcessValue(value = TestValues.TEST_STRING_PROCESS_VALUE, type = String.class, delegate = TestValues.TEST_DELEGATE_FIRST_NAME),
                                   @ProcessValue(value = TestValues.TEST_STRING_PROCESS_VALUE_2, type = String.class, delegate = TestValues.TEST_DELEGATE_SECOND_NAME)
                           }) String testString) {
        return processKey;
    }

}
