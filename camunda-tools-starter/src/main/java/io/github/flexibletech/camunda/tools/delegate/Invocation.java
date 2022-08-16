package io.github.flexibletech.camunda.tools.delegate;

import io.github.flexibletech.camunda.tools.common.Constants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Represents invocation of delegate method.
 */
public final class Invocation {
    private final Method delegateMethod;
    private final Object bean;
    private final Object[] args;

    private Invocation(Method delegateMethod, Object bean, Object[] args) {
        this.delegateMethod = delegateMethod;
        this.bean = bean;
        this.args = args;
    }

    /**
     * Execute delegate method with process key.
     *
     * @param processKey process key
     * @return result of delegate method
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException
     */
    public Object execute(String processKey) throws InvocationTargetException, IllegalAccessException {
        if(StringUtils.isEmpty(processKey))
            throw new IllegalArgumentException("processKey can't be null or empty");

        List<Object> invokeArgs = Arrays.asList(this.args);
        int index = invokeArgs.indexOf(Constants.BUSINESS_KEY_VALUE);
        invokeArgs.set(index, processKey);

        return this.delegateMethod.invoke(this.bean, invokeArgs.toArray(Object[]::new));
    }

    /**
     * Factory method for {@link Invocation}
     *
     * @param method delegate method
     * @param bean   bean with delegate methods
     * @param args   args of delegate methods
     * @return created invocation
     */
    public static Invocation newInvocation(Method method, Object bean, Object[] args) {
        assert method != null;
        assert bean != null;
        assert args != null;

        if(ArrayUtils.isEmpty(args))
            throw new IllegalArgumentException("Args can't be empty");

        return new Invocation(method, bean, args);
    }
}
