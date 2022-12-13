package io.github.flexibletech.camunda.tools.process.values;

import io.github.flexibletech.camunda.tools.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * The component, responsible for determining the values of delegate function arguments to be used in the process.
 */
@Component
public class ProcessValuesDefiner {
    private final GenericApplicationContext genericApplicationContext;

    private static final String VALUE_OF_ENUM_METHOD = "valueOf";

    private final Logger log = LoggerFactory.getLogger(ProcessValuesDefiner.class);

    public ProcessValuesDefiner(GenericApplicationContext genericApplicationContext) {
        this.genericApplicationContext = genericApplicationContext;
    }

    /**
     * Gets an array of process values from the delegate method's argument array.
     *
     * @param parameters array with delegate method arguments
     * @return array of process values
     */
    public Object[] defineProcessValues(Parameter[] parameters, String delegateName) {
        var delegateMethodArgumentValues = new ArrayList<>();

        for (Parameter parameter : parameters) {
            try {
                delegateMethodArgumentValues.add(defineBeanProcessValueForDelegate(parameter, delegateName));
                delegateMethodArgumentValues.add(defineStringOrEnumProcessValueForDelegate(parameter, delegateName));
                delegateMethodArgumentValues.add(defineProcessKeyValue(parameter));
                delegateMethodArgumentValues.add(defineStringOrEnumProcessValue(parameter));
                delegateMethodArgumentValues.add(defineBeanProcessValue(parameter));

            } catch (InvocationTargetException | IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }
        return delegateMethodArgumentValues.stream()
                .filter(Objects::nonNull)
                .toArray();
    }

    private Object defineStringOrEnumProcessValueForDelegate(Parameter parameter, String delegateName) throws InvocationTargetException, IllegalAccessException {
        if (parameter.isAnnotationPresent(ProcessValues.class) && StringUtils.isNotBlank(delegateName)) {
            var processValuesAnnotation = parameter.getAnnotation(ProcessValues.class);

            var optionalProcessValueAnnotation = Arrays.stream(processValuesAnnotation.values())
                    .filter(processValue -> processValue.delegate().equals(delegateName))
                    .findAny();

            if (optionalProcessValueAnnotation.isPresent()) {
                var processValueAnnotation = optionalProcessValueAnnotation.get();

                checkProcessValueTypes(processValueAnnotation.type());
                if (processValueAnnotation.type().isEnum())
                    return defineEnumProcessValue(processValueAnnotation.type(), processValueAnnotation.value());
                return processValueAnnotation.value();
            }
        }
        return null;
    }

    private Object defineBeanProcessValueForDelegate(Parameter parameter, String delegateName) {
        if (parameter.isAnnotationPresent(BeanProcessValues.class) && StringUtils.isNotBlank(delegateName)) {
            var beanProcessValuesAnnotation = parameter.getAnnotation(BeanProcessValues.class);

            var beanProcessValueAnnotation = Arrays.stream(beanProcessValuesAnnotation.values())
                    .filter(beanProcessValue -> beanProcessValue.delegate().equals(delegateName))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Need specify BeanProcessValue"));

            return genericApplicationContext.getBean(beanProcessValueAnnotation.value());
        }
        return null;
    }

    /**
     * Define process value for argument with string or enum type.
     *
     * @param parameter argument with string or enum type
     * @return process value
     */
    private Object defineStringOrEnumProcessValue(Parameter parameter) throws InvocationTargetException, IllegalAccessException {
        if (parameter.isAnnotationPresent(ProcessValue.class)) {
            var annotation = parameter.getAnnotation(ProcessValue.class);

            checkProcessValueTypes(annotation.type());
            if (annotation.type().isEnum()) return defineEnumProcessValue(annotation.type(), annotation.value());
            return annotation.value();
        }
        return null;
    }

    private void checkProcessValueTypes(Class<?> clazz) {
        if (clazz != String.class && !clazz.isEnum())
            throw new IllegalArgumentException("Process value type must be String or Enum");
    }

    /**
     * If there is a parameter marked with the ProcessKeyValue annotation in the parameter
     * list of the delegate method, return Constants.BUSINESS_KEY_VALUE as the value of this parameter.
     * In the future, this value will be replaced by the real value of the process key
     * obtained from the camunda process variables.
     *
     * @param parameter Delegate method parameter
     * @return Constants.BUSINESS_KEY_VALUE if there is a parameter marked with the ProcessKeyValue annotation
     */
    private Object defineProcessKeyValue(Parameter parameter) {
        if (parameter.isAnnotationPresent(ProcessKeyValue.class))
            return Constants.BUSINESS_KEY_VALUE;
        return null;
    }

    /**
     * Define process value for argument with enum type.
     *
     * @param enumClass        enum class
     * @param enumProcessValue enum process value
     * @return process value
     */
    private Object defineEnumProcessValue(Class<?> enumClass, String enumProcessValue) throws InvocationTargetException, IllegalAccessException {
        var enumMethods = enumClass.getMethods();

        var valueOfMethod = Arrays.stream(enumMethods)
                .filter(m -> m.getName().equals(VALUE_OF_ENUM_METHOD))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("There is no valueOf method in this enum"));

        return valueOfMethod.invoke(null, enumProcessValue);
    }

    /**
     * Define process value for spring bean argument.
     *
     * @param parameter spring bean argument
     * @return process value
     */
    private Object defineBeanProcessValue(Parameter parameter) {
        if (parameter.isAnnotationPresent(BeanProcessValue.class)) {
            var annotation = parameter.getAnnotation(BeanProcessValue.class);
            return genericApplicationContext.getBean(annotation.value());
        }
        return null;
    }
}
