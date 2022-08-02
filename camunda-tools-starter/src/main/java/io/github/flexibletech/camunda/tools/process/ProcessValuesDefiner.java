package io.github.flexibletech.camunda.tools.process;

import io.github.flexibletech.camunda.tools.delegate.BeanProcessValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
     * @return           array of process values
     */
    public Object[] defineProcessValues(Parameter[] parameters) {
        List<Object> actionArgumentValues = new ArrayList<>();

        for (Parameter parameter : parameters) {
            try {
                actionArgumentValues.add(defineProcessValue(parameter));
                actionArgumentValues.add(defineBeanProcessValue(parameter));

            } catch (InvocationTargetException | IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }
        return actionArgumentValues
                .stream()
                .filter(Objects::nonNull)
                .toArray();
    }

    /**
     * Define process value for argument with string or enum type.
     *
     * @param parameter argument with string or enum type
     * @return          process value
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException
     */
    private Object defineProcessValue(Parameter parameter) throws InvocationTargetException, IllegalAccessException {
        if (parameter.isAnnotationPresent(ProcessValue.class)) {
            ProcessValue annotation = parameter.getAnnotation(ProcessValue.class);

            checkProcessValueTypes(annotation.type());
            if (annotation.type().isEnum()) return defineEnumProcessValue(annotation.type(), annotation.value());
            return annotation.value();
        }
        return null;
    }

    private void checkProcessValueTypes(Class<?> clazz){
        if(clazz != String.class && !clazz.isEnum())
            throw new IllegalArgumentException("Process value type must be String or Enum");
    }

    /**
     * Define process value for argument with enum type.
     *
     * @param enumClass         enum class
     * @param enumProcessValue  enum process value
     * @return                  process value
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException
     */
    private Object defineEnumProcessValue(Class<?> enumClass, String enumProcessValue) throws InvocationTargetException, IllegalAccessException {
        Method[] enumMethods = enumClass.getMethods();

        Method valueOfMethod = Arrays.stream(enumMethods)
                .filter(m -> m.getName().equals(VALUE_OF_ENUM_METHOD))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("There is no valueOf method in this enum"));

        return valueOfMethod.invoke(null, enumProcessValue);
    }

    /**
     * Define process value for spring bean argument.
     *
     * @param parameter spring bean argument
     * @return          process value
     */
    private Object defineBeanProcessValue(Parameter parameter) {
        if (parameter.isAnnotationPresent(BeanProcessValue.class)) {
            BeanProcessValue annotation = parameter.getAnnotation(BeanProcessValue.class);
            return genericApplicationContext.getBean(annotation.value());
        }
        return null;
    }

}
