package io.github.flexibletech.camunda.tools.delegate;

import io.github.flexibletech.camunda.tools.Application;
import io.github.flexibletech.camunda.tools.values.TestValues;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.GenericApplicationContext;

@SpringBootTest(classes = Application.class)
public class SpringBeanDelegateRegistrationIT {
    @Autowired
    private GenericApplicationContext applicationContext;
    @MockBean
    private RuntimeService runtimeService;
    @MockBean
    private TaskService taskService;

    @Test
    public void shouldRegisterSpringBean() {
        var firstDelegate = applicationContext.getBean(TestValues.TEST_DELEGATE_FIRST_NAME);
        var secondDelegate = applicationContext.getBean(TestValues.TEST_DELEGATE_SECOND_NAME);
        var thirdDelegate = applicationContext.getBean(TestValues.TEST_DELEGATE_THIRD_NAME);
        var fourDelegate = applicationContext.getBean(TestValues.TEST_DELEGATE_FOUR_NAME);
        var fiveDelegate = applicationContext.getBean(TestValues.TEST_DELEGATE_FIVE_NAME);
        var sixDelegate = applicationContext.getBean(TestValues.TEST_DELEGATE_SIX_NAME);

        Assertions.assertNotNull(firstDelegate);
        Assertions.assertNotNull(secondDelegate);
        Assertions.assertNotNull(thirdDelegate);
        Assertions.assertNotNull(fourDelegate);
        Assertions.assertNotNull(fiveDelegate);
        Assertions.assertNotNull(sixDelegate);
    }
}
