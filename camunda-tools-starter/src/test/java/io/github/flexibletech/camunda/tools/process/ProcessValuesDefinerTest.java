package io.github.flexibletech.camunda.tools.process;

import io.github.flexibletech.camunda.tools.common.Constants;
import io.github.flexibletech.camunda.tools.process.values.ProcessValuesDefiner;
import io.github.flexibletech.camunda.tools.utils.ReflectionUtils;
import io.github.flexibletech.camunda.tools.values.TestEnum;
import io.github.flexibletech.camunda.tools.values.TestValues;
import io.github.flexibletech.camunda.tools.values.beans.BeanVariable;
import io.github.flexibletech.camunda.tools.values.beans.TestBean;
import io.github.flexibletech.camunda.tools.values.beans.TestBeanWithWrongProcessValueType;
import org.apache.commons.lang3.StringUtils;
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
    public void shouldDefineEnumProcessValue() {
        var processValues = processValuesDefiner.defineProcessValues
                (ReflectionUtils.findMethod("doActionThird", TestBean.class).getParameters(),
                        StringUtils.EMPTY);

        Assertions.assertEquals(processValues[1], TestEnum.VALUE1);
    }

    @Test
    public void shouldDefineStringProcessValue() {
        var processValues = processValuesDefiner.defineProcessValues(
                ReflectionUtils.findMethod("doActionFour", TestBean.class).getParameters(),
                StringUtils.EMPTY);

        Assertions.assertEquals(processValues[1], TestValues.TEST_STRING_PROCESS_VALUE);
    }

    @Test
    public void shouldDefineStringProcessAndEnumValues() {
        var processValues = processValuesDefiner.defineProcessValues(
                ReflectionUtils.findMethod("doActionFive", TestBean.class).getParameters(),
                StringUtils.EMPTY);

        Assertions.assertEquals(processValues[1], TestEnum.VALUE1);
        Assertions.assertEquals(processValues[2], TestValues.TEST_STRING_PROCESS_VALUE);
    }

    @Test
    public void shouldDefineBeanProcessValue() {
        Mockito.when(genericApplicationContext.getBean(ArgumentMatchers.anyString())).thenReturn(new BeanVariable());

        var processValues = processValuesDefiner.defineProcessValues(
                ReflectionUtils.findMethod("doActionSix", TestBean.class).getParameters(),
                StringUtils.EMPTY);

        var beanVariable = processValues[1];
        Assertions.assertNotNull(beanVariable);
        Assertions.assertTrue(beanVariable instanceof BeanVariable);
    }

    @Test
    public void shouldDefineProcessKeyValue() {
        Mockito.when(genericApplicationContext.getBean(ArgumentMatchers.anyString())).thenReturn(new BeanVariable());

        var processValues = processValuesDefiner.defineProcessValues(
                ReflectionUtils.findMethod("doActionSix", TestBean.class).getParameters(),
                StringUtils.EMPTY);

        var keyValue = processValues[0];
        Assertions.assertNotNull(keyValue);
        Assertions.assertEquals(keyValue, Constants.BUSINESS_KEY_VALUE);
    }

    @Test
    public void shouldThrowExceptionOnWrongProcessValueType() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> processValuesDefiner.defineProcessValues(
                        ReflectionUtils.findMethod("doAction", TestBeanWithWrongProcessValueType.class).getParameters(),
                        StringUtils.EMPTY));
    }

    @Test
    public void shouldDefineStringProcessValuesForEightDelegate(){
        var processValues = processValuesDefiner.defineProcessValues(
                ReflectionUtils.findMethod("doActionTwelve", TestBean.class).getParameters(),
                TestValues.TEST_DELEGATE_EIGHT_NAME);

        Assertions.assertEquals(processValues[1], TestValues.TEST_STRING_PROCESS_VALUE);
    }

    @Test
    public void shouldDefineStringProcessValuesForNineDelegate(){
        var processValues = processValuesDefiner.defineProcessValues(
                ReflectionUtils.findMethod("doActionTwelve", TestBean.class).getParameters(),
                TestValues.TEST_DELEGATE_NINE_NAME);

        Assertions.assertEquals(processValues[1], TestValues.TEST_STRING_PROCESS_VALUE_2);
    }


}
