package io.github.flexibletech.camunda.tools.values.beans;

import io.github.flexibletech.camunda.tools.delegate.BeanProcessValue;
import io.github.flexibletech.camunda.tools.delegate.Delegate;
import io.github.flexibletech.camunda.tools.process.ProcessValue;
import io.github.flexibletech.camunda.tools.values.TestEnum;
import io.github.flexibletech.camunda.tools.values.TestValues;
import org.springframework.stereotype.Component;

@Component
public class TestBean {

    @Delegate(beanName = TestValues.TEST_DELEGATE_FIRST_NAME, key = TestValues.PROCESS_KEY)
    public int doActionFirst(String processKey, int param) {
        return ++param;
    }

    @Delegate(beanName = TestValues.TEST_DELEGATE_SECOND_NAME, key = TestValues.PROCESS_KEY)
    public String doActionSecond(String processKey) {
        return processKey;
    }

    @Delegate(beanName = TestValues.TEST_DELEGATE_THIRD_NAME, key = TestValues.PROCESS_KEY)
    public String doActionThird(String processKey,
                                @ProcessValue(value = "VALUE1", type = TestEnum.class) TestEnum testEnum) {
        return processKey;
    }

    @Delegate(beanName = TestValues.TEST_DELEGATE_FOUR_NAME, key = TestValues.PROCESS_KEY)
    public String doActionFour(String processKey,
                               @ProcessValue(value = TestValues.TEST_STRING_PROCESS_VALUE, type = String.class) String testString) {
        return processKey;
    }

    @Delegate(beanName = TestValues.TEST_DELEGATE_FIVE_NAME, key = TestValues.PROCESS_KEY)
    public String doActionFive(String processKey,
                               @ProcessValue(value = "VALUE1", type = TestEnum.class) TestEnum testEnum,
                               @ProcessValue(value = TestValues.TEST_STRING_PROCESS_VALUE, type = String.class) String testString) {
        return processKey;
    }

    @Delegate(beanName = TestValues.TEST_DELEGATE_SIX_NAME, key = TestValues.PROCESS_KEY)
    public String doActionSix(String processKey,
                              @BeanProcessValue(value = "beanVariable") BeanVariable beanVariable) {
        return processKey;
    }

    public String processKey() {
        return TestValues.PROCESS_KEY;
    }

}
