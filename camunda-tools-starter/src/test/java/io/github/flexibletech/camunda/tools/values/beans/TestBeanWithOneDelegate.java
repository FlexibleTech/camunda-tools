package io.github.flexibletech.camunda.tools.values.beans;

import io.github.flexibletech.camunda.tools.delegate.Delegate;
import io.github.flexibletech.camunda.tools.process.ProcessKeyValue;
import io.github.flexibletech.camunda.tools.values.TestValues;

public class TestBeanWithOneDelegate {

    @Delegate(beanName = TestValues.TEST_DELEGATE_FIRST_NAME, key = TestValues.PROCESS_KEY)
    public int doAction(@ProcessKeyValue String processKey, int param) {
        return ++param;
    }

}
