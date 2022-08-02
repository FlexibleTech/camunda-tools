package io.github.flexibletech.camunda.tools.delegate;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
        if (ArrayUtils.isEmpty(this.args))
            return this.delegateMethod.invoke(this.bean, processKey);

        return this.delegateMethod.invoke(this.bean, convertArgsToMethodParams(processKey));
    }

    private Object[] convertArgsToMethodParams(String processKey) {
        LinkedList<Object> invokeArgs = new LinkedList<>(List.of(this.args));
        if (Objects.nonNull(processKey)) invokeArgs.addFirst(processKey);

        return invokeArgs.toArray(Object[]::new);
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

        return new Invocation(method, bean, args);
    }
}
