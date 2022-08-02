package io.github.flexibletech.camunda.tools.process;

import io.github.flexibletech.camunda.tools.utils.ReflectionUtils;
import io.github.flexibletech.camunda.tools.values.TestEnum;
import io.github.flexibletech.camunda.tools.values.TestValues;
import io.github.flexibletech.camunda.tools.values.beans.BeanVariable;
import io.github.flexibletech.camunda.tools.values.beans.TestBean;
import io.github.flexibletech.camunda.tools.values.beans.TestBeanWithWrongProcessValueType;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.GenericApplicationContext;

@ExtendWith(MockitoExtension.class)
public class ProcessValuesDefinerTest {
    @Mock
    private GenericApplicationContext genericApplicationContext;

    @InjectMocks
    private ProcessValuesDefiner processValuesDefiner;

    @Test
    public void shouldDefineEmptyProcessValuesArray() {
        var processValues = processValuesDefiner.defineProcessValues(
                ReflectionUtils.findMethod("doActionFirst", TestBean.class).getParameters()
        );

        Assertions.assertTrue(ArrayUtils.isEmpty(processValues));
    }

    @Test
    public void shouldDefineEnumProcessValue() {
        var processValues = processValuesDefiner.defineProcessValues(
                ReflectionUtils.findMethod("doActionThird", TestBean.class).getParameters()
        );

        Assertions.assertEquals(processValues[0], TestEnum.VALUE1);
    }

    @Test
    public void shouldDefineStringProcessValue() {
        var processValues = processValuesDefiner.defineProcessValues(
                ReflectionUtils.findMethod("doActionFour", TestBean.class).getParameters()
        );

        Assertions.assertEquals(processValues[0], TestValues.TEST_STRING_PROCESS_VALUE);
    }

    @Test
    public void shouldDefineStringProcessAndEnumValues() {
        var processValues = processValuesDefiner.defineProcessValues(
                ReflectionUtils.findMethod("doActionFive", TestBean.class).getParameters()
        );

        Assertions.assertEquals(processValues[0], TestEnum.VALUE1);
        Assertions.assertEquals(processValues[1], TestValues.TEST_STRING_PROCESS_VALUE);
    }

    @Test
    public void shouldDefineBeanProcessVariable() {
        Mockito.when(genericApplicationContext.getBean(ArgumentMatchers.anyString())).thenReturn(new BeanVariable());

        var processValues = processValuesDefiner.defineProcessValues(
                ReflectionUtils.findMethod("doActionSix", TestBean.class).getParameters()
        );

        var beanVariable = processValues[0];
        Assertions.assertNotNull(beanVariable);
        Assertions.assertTrue(beanVariable instanceof BeanVariable);
    }

    @Test
    public void shouldThrowExceptionOnWrongProcessValueType() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> processValuesDefiner.defineProcessValues(
                        ReflectionUtils.findMethod("doAction", TestBeanWithWrongProcessValueType.class).getParameters()
                )
        );
    }
}
