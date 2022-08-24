package io.github.flexibletech.camunda.tools.values.beans;

import io.github.flexibletech.camunda.tools.delegate.Delegates;
import io.github.flexibletech.camunda.tools.process.values.BeanProcessValue;
import io.github.flexibletech.camunda.tools.delegate.Delegate;
import io.github.flexibletech.camunda.tools.process.values.ProcessKeyValue;
import io.github.flexibletech.camunda.tools.process.values.ProcessValue;
import io.github.flexibletech.camunda.tools.process.values.ProcessValues;
import io.github.flexibletech.camunda.tools.process.variables.ProcessVariable;
import io.github.flexibletech.camunda.tools.task.receive.ReceiveTask;
import io.github.flexibletech.camunda.tools.task.user.UserTask;
import io.github.flexibletech.camunda.tools.values.TestDataFactory;
import io.github.flexibletech.camunda.tools.values.TestEnum;
import io.github.flexibletech.camunda.tools.values.TestOutputObject;
import io.github.flexibletech.camunda.tools.values.TestValues;
import org.springframework.stereotype.Component;

@Component
public class TestBean {

    @Delegate(beanName = TestValues.TEST_DELEGATE_FIRST_NAME, key = TestValues.PROCESS_KEY)
    public int doActionFirst(@ProcessKeyValue String processKey, int param) {
        return ++param;
    }

    @Delegate(beanName = TestValues.TEST_DELEGATE_SECOND_NAME, key = TestValues.PROCESS_KEY)
    public String doActionSecond(@ProcessKeyValue String processKey) {
        return processKey;
    }

    @Delegate(beanName = TestValues.TEST_DELEGATE_THIRD_NAME, key = TestValues.PROCESS_KEY)
    public String doActionThird(@ProcessKeyValue String processKey,
                                @ProcessValue(value = "VALUE1", type = TestEnum.class) TestEnum testEnum) {
        return processKey;
    }

    @Delegate(beanName = TestValues.TEST_DELEGATE_FOUR_NAME, key = TestValues.PROCESS_KEY)
    public String doActionFour(@ProcessKeyValue String processKey,
                               @ProcessValue(value = TestValues.TEST_STRING_PROCESS_VALUE, type = String.class) String testString) {
        return processKey;
    }

    @Delegate(beanName = TestValues.TEST_DELEGATE_FIVE_NAME, key = TestValues.PROCESS_KEY)
    public String doActionFive(@ProcessKeyValue String processKey,
                               @ProcessValue(value = "VALUE1", type = TestEnum.class) TestEnum testEnum,
                               @ProcessValue(value = TestValues.TEST_STRING_PROCESS_VALUE, type = String.class) String testString) {
        return processKey;
    }

    @Delegate(beanName = TestValues.TEST_DELEGATE_SIX_NAME, key = TestValues.PROCESS_KEY)
    public String doActionSix(@ProcessKeyValue String processKey,
                              @BeanProcessValue(value = "beanVariable") BeanVariable beanVariable) {
        return processKey;
    }

    @UserTask(definitionKey = TestValues.USER_TASK_FIRST,
            variables =
                    {@ProcessVariable(name = TestValues.CLASS_NAME_VARIABLE, value = TestValues.CLASS_NAME_VARIABLE_EXPRESSION)})
    public TestOutputObject doActionSeven(@ProcessKeyValue String processKey) {
        return TestDataFactory.newTestOutputObject();
    }

    @UserTask(definitionKey = TestValues.USER_TASK_SECOND)
    public void doActionEight(@ProcessKeyValue String processKey) {
    }

    @ReceiveTask(definitionKey = TestValues.RECEIVE_TASK_FIRST,
            variables = {@ProcessVariable(name = TestValues.CLASS_NAME_VARIABLE,
                    value = TestValues.CLASS_NAME_VARIABLE_EXPRESSION)})
    public void doActionNine(@ProcessKeyValue String processKey) {
    }

    @ReceiveTask(definitionKey = TestValues.RECEIVE_TASK_FIRST)
    public void doActionTen(@ProcessKeyValue String processKey) {
    }

    @Delegate(beanName = TestValues.TEST_DELEGATE_SEVEN_NAME, key = TestValues.PROCESS_KEY, throwBpmnError = true)
    public void doActionEleven(@ProcessKeyValue String processKey) {
        throw new RuntimeException();
    }

    @Delegates(
            values = {
                    @Delegate(beanName = TestValues.TEST_DELEGATE_EIGHT_NAME, key = TestValues.PROCESS_KEY),
                    @Delegate(beanName = TestValues.TEST_DELEGATE_NINE_NAME, key = TestValues.PROCESS_KEY)
            }
    )
    public void doActionTwelve(@ProcessKeyValue String processKey,
                               @ProcessValues(values = {
                                       @ProcessValue(value = TestValues.TEST_STRING_PROCESS_VALUE, type = String.class, delegate = TestValues.TEST_DELEGATE_EIGHT_NAME),
                                       @ProcessValue(value = TestValues.TEST_STRING_PROCESS_VALUE_2, type = String.class, delegate = TestValues.TEST_DELEGATE_NINE_NAME)
                               }) String testString) {
    }

    @Delegates(
            values = {
                    @Delegate(beanName = TestValues.TEST_DELEGATE_TEN_NAME, key = TestValues.PROCESS_KEY),
                    @Delegate(beanName = TestValues.TEST_DELEGATE_ELEVEN_NAME, key = TestValues.PROCESS_KEY)
            }
    )
    public void doActionThirteen(@ProcessKeyValue String processKey,
                                 @ProcessValues(values = {
                                         @ProcessValue(value = TestValues.TEST_STRING_PROCESS_VALUE, type = String.class, delegate = TestValues.TEST_DELEGATE_EIGHT_NAME),
                                         @ProcessValue(value = TestValues.TEST_STRING_PROCESS_VALUE_2, type = String.class, delegate = TestValues.TEST_DELEGATE_NINE_NAME)
                                 }) String testString) {
    }
    public String processKey() {
        return TestValues.PROCESS_KEY;
    }

}
