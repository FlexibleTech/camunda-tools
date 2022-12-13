package io.github.flexibletech.camunda.tools.delegate;

import io.github.flexibletech.camunda.tools.process.values.ProcessValuesDefiner;
import io.github.flexibletech.camunda.tools.values.TestValues;
import io.github.flexibletech.camunda.tools.values.beans.TestBeanWithMultipleDelegate;
import io.github.flexibletech.camunda.tools.values.beans.TestBeanWithOneDelegate;
import io.github.flexibletech.camunda.tools.values.beans.TestBeanWithOutputVariableExpression;
import io.github.flexibletech.camunda.tools.values.beans.TestBeanWithOutputVariableFunctionCall;
import org.apache.commons.collections4.MapUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class DelegatesBeanRegistrarTest {
    @Mock
    private GenericApplicationContext genericApplicationContext;

    @InjectMocks
    private DelegatesBeanRegistrar delegatesBeanRegistrar;

    @InjectMocks
    private ProcessValuesDefiner processValuesDefiner;
    private TemplateDelegate templateDelegate;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(delegatesBeanRegistrar, "processValuesDefiner", processValuesDefiner);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldRegisterDelegateForOneAction() {
        var templateDelegate = new TemplateDelegate();
        Mockito.doNothing().when(genericApplicationContext)
                .registerBean(TestValues.TEST_DELEGATE_FIRST_NAME, TemplateDelegate.class);
        Mockito.lenient().when(genericApplicationContext.getBean(TestValues.TEST_DELEGATE_FIRST_NAME))
                .thenThrow(NoSuchBeanDefinitionException.class);
        Mockito.lenient().when(genericApplicationContext.getBean(TestValues.TEST_DELEGATE_FIRST_NAME, TemplateDelegate.class))
                .thenReturn(templateDelegate);

        delegatesBeanRegistrar.registerDelegatesForBean(new TestBeanWithOneDelegate());

        // Assert delegate registration
        Mockito.verify(genericApplicationContext, Mockito.times(1))
                .registerBean(TestValues.TEST_DELEGATE_FIRST_NAME, TemplateDelegate.class);

        // Assert invocation
        var invocation = (Invocation) ReflectionTestUtils.getField(templateDelegate, "invocation");
        Assertions.assertNotNull(invocation);

        // Assert variables
        var variables = (Map<String, String>) ReflectionTestUtils.getField(templateDelegate, "variables");
        Assertions.assertTrue(MapUtils.isEmpty(variables));

        // Assert process key name
        var processKeyName = (String) ReflectionTestUtils.getField(templateDelegate, "processKey");
        Assertions.assertEquals(processKeyName, TestValues.PROCESS_KEY);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldRegisterMultipleDelegatesForOneAction() {
        var firstDelegate = new TemplateDelegate();
        var secondDelegate = new TemplateDelegate();
        Mockito.doNothing().when(genericApplicationContext)
                .registerBean(TestValues.TEST_DELEGATE_FIRST_NAME, TemplateDelegate.class);
        Mockito.doNothing().when(genericApplicationContext)
                .registerBean(TestValues.TEST_DELEGATE_SECOND_NAME, TemplateDelegate.class);
        Mockito.lenient().when(genericApplicationContext.getBean(TestValues.TEST_DELEGATE_FIRST_NAME))
                .thenThrow(NoSuchBeanDefinitionException.class);
        Mockito.lenient().when(genericApplicationContext.getBean(TestValues.TEST_DELEGATE_FIRST_NAME, TemplateDelegate.class))
                .thenReturn(firstDelegate);
        Mockito.lenient().when(genericApplicationContext.getBean(TestValues.TEST_DELEGATE_SECOND_NAME))
                .thenThrow(NoSuchBeanDefinitionException.class);
        Mockito.lenient().when(genericApplicationContext.getBean(TestValues.TEST_DELEGATE_SECOND_NAME, TemplateDelegate.class))
                .thenReturn(secondDelegate);

        delegatesBeanRegistrar.registerDelegatesForBean(new TestBeanWithMultipleDelegate());

        // Assert delegates registration
        Mockito.verify(genericApplicationContext, Mockito.times(1))
                .registerBean(TestValues.TEST_DELEGATE_FIRST_NAME, TemplateDelegate.class);
        Mockito.verify(genericApplicationContext, Mockito.times(1))
                .registerBean(TestValues.TEST_DELEGATE_SECOND_NAME, TemplateDelegate.class);

        // Assert invocations
        var firstDelegateInvocation = (Invocation) ReflectionTestUtils.getField(firstDelegate, "invocation");
        var secondDelegateInvocation = (Invocation) ReflectionTestUtils.getField(firstDelegate, "invocation");
        Assertions.assertNotNull(firstDelegateInvocation);
        Assertions.assertNotNull(secondDelegateInvocation);

        // Assert variables
        var firstDelegateVariables = (Map<String, String>) ReflectionTestUtils.getField(firstDelegate, "variables");
        var secondDelegateVariables = (Map<String, String>) ReflectionTestUtils.getField(firstDelegate, "variables");
        Assertions.assertTrue(MapUtils.isEmpty(firstDelegateVariables));
        Assertions.assertTrue(MapUtils.isEmpty(secondDelegateVariables));

        // Assert process key names
        var firstDelegateProcessKeyName = (String) ReflectionTestUtils.getField(firstDelegate, "processKey");
        var secondDelegateProcessKeyName = (String) ReflectionTestUtils.getField(firstDelegate, "processKey");
        Assertions.assertEquals(firstDelegateProcessKeyName, TestValues.PROCESS_KEY);
        Assertions.assertEquals(secondDelegateProcessKeyName, TestValues.PROCESS_KEY);
    }

    @Test
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public void shouldRegisterDelegateWithOutputVariableExpression() {
        var templateDelegate = new TemplateDelegate();
        Mockito.lenient().when(genericApplicationContext.getBean(TestValues.TEST_DELEGATE_FIRST_NAME))
                .thenThrow(NoSuchBeanDefinitionException.class);
        Mockito.doNothing().when(genericApplicationContext)
                .registerBean(TestValues.TEST_DELEGATE_FIRST_NAME, TemplateDelegate.class);
        Mockito.lenient().when(genericApplicationContext.getBean(TestValues.TEST_DELEGATE_FIRST_NAME, TemplateDelegate.class))
                .thenReturn(templateDelegate);

        delegatesBeanRegistrar.registerDelegatesForBean(new TestBeanWithOutputVariableExpression());

        // Assert delegate registration
        Mockito.verify(genericApplicationContext, Mockito.times(1))
                .registerBean(TestValues.TEST_DELEGATE_FIRST_NAME, TemplateDelegate.class);

        // Assert invocation
        var invocation = (Invocation) ReflectionTestUtils.getField(templateDelegate, "invocation");
        Assertions.assertNotNull(invocation);

        // Assert variables
        var variables = (Map<String, String>) ReflectionTestUtils.getField(templateDelegate, "variables");
        Assertions.assertEquals(variables.get(TestValues.TEST_OUTPUT_OBJECT_VARIABLE_NAME),
                TestValues.TEST_OUTPUT_OBJECT_VARIABLE_VALUE);

        // Assert process key name
        var processKeyName = (String) ReflectionTestUtils.getField(templateDelegate, "processKey");
        Assertions.assertEquals(processKeyName, TestValues.PROCESS_KEY);
    }

    @Test
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public void shouldRegisterDelegateWithOutputVariableFunctionCall() {
        var templateDelegate = new TemplateDelegate();
        Mockito.lenient().when(genericApplicationContext.getBean(TestValues.TEST_DELEGATE_FIRST_NAME))
                .thenThrow(NoSuchBeanDefinitionException.class);
        Mockito.doNothing().when(genericApplicationContext)
                .registerBean(TestValues.TEST_DELEGATE_FIRST_NAME, TemplateDelegate.class);
        Mockito.lenient().when(genericApplicationContext.getBean(TestValues.TEST_DELEGATE_FIRST_NAME, TemplateDelegate.class))
                .thenReturn(templateDelegate);

        delegatesBeanRegistrar.registerDelegatesForBean(new TestBeanWithOutputVariableFunctionCall());

        // Assert delegate registration
        Mockito.verify(genericApplicationContext, Mockito.times(1))
                .registerBean(TestValues.TEST_DELEGATE_FIRST_NAME, TemplateDelegate.class);

        // Assert invocation
        var invocation = (Invocation) ReflectionTestUtils.getField(templateDelegate, "invocation");
        Assertions.assertNotNull(invocation);


        // Assert variables
        var variables = (Map<String, String>) ReflectionTestUtils.getField(templateDelegate, "variables");
        Assertions.assertEquals(variables.get(TestValues.CLASS_NAME_VARIABLE), TestValues.CLASS_NAME_VARIABLE_EXPRESSION);

        // Assert process key name
        var processKeyName = (String) ReflectionTestUtils.getField(templateDelegate, "processKey");
        Assertions.assertEquals(processKeyName, TestValues.PROCESS_KEY);
    }

    @Test
    public void shouldDontRegisterAlreadyRegisteredDelegate() {
        var templateDelegate = new TemplateDelegate();
        Mockito.lenient().when(genericApplicationContext.getBean(TestValues.TEST_DELEGATE_FIRST_NAME))
                .thenReturn(templateDelegate);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> delegatesBeanRegistrar.registerDelegatesForBean(new TestBeanWithOneDelegate()));
    }
}
