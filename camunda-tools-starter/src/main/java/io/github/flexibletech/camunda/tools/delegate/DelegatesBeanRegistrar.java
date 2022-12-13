package io.github.flexibletech.camunda.tools.delegate;

import io.github.flexibletech.camunda.tools.process.values.ProcessValuesDefiner;
import io.github.flexibletech.camunda.tools.process.variables.ProcessVariablesCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * The main component responsible for finding delegate methods in the bean and registering them in the spring context.
 */
@Component
class DelegatesBeanRegistrar {
    private final GenericApplicationContext applicationContext;
    private final ProcessValuesDefiner processValuesDefiner;

    private final Logger log = LoggerFactory.getLogger(DelegatesBeanRegistrar.class);

    @Autowired
    private DelegatesBeanRegistrar(GenericApplicationContext applicationContext, ProcessValuesDefiner processValuesDefiner) {
        this.applicationContext = applicationContext;
        this.processValuesDefiner = processValuesDefiner;
    }

    /**
     * Registers delegates in the spring context for the given bean.
     *
     * @param bean Bean for which delegates will be registered.
     */
    void registerDelegatesForBean(Object bean) {
        var delegateMethods = bean.getClass().getMethods();

        for (var delegateMethod : delegateMethods) {
            var delegateAnnotation = AnnotationUtils.findAnnotation(delegateMethod, Delegate.class);
            var delegatesAnnotation = AnnotationUtils.findAnnotation(delegateMethod, Delegates.class);

            registerDelegate(delegateMethod, bean, delegateAnnotation);
            registerDelegates(delegateMethod, bean, delegatesAnnotation);
        }
    }

    private void registerDelegates(Method delegateMethod, Object bean, Delegates delegatesAnnotation) {
        if (Objects.nonNull(delegatesAnnotation)) {
            var delegates = delegatesAnnotation.values();

            Arrays.stream(delegates).forEach(delegate -> registerDelegate(delegateMethod, bean, delegate));
        }
    }

    private void registerDelegate(Method delegateMethod, Object bean, Delegate delegate) {
        if (Objects.nonNull(delegate)) {

            var processVariables = delegate.variables();
            var processKey = delegate.key();
            var beanName = delegate.beanName();
            var processValues = processValuesDefiner.defineProcessValues(delegateMethod.getParameters(), delegate.beanName());

            var invocation = Invocation.newInvocation(delegateMethod, bean, processValues, delegate.throwBpmnError());
            var variables = Arrays.stream(processVariables).collect(ProcessVariablesCollector.toValuesMap());

            registerBean(beanName, invocation, processKey, variables);

            log.info("Delegate {} has been registered", beanName);
        }
    }

    /**
     * Registers a delegate bean in the context of the spring.
     * The bean will only be registered if it is not present in the context.
     *
     * @param beanName   The name of the bean to be used for the delegate.
     * @param invocation {@link io.github.flexibletech.camunda.tools.delegate.Invocation}
     * @param processKey The key with which the process will be launched in camunda.
     * @param variables  Process variables.
     */
    private void registerBean(String beanName, Invocation invocation, String processKey, Map<String, String> variables) {
        try {
            applicationContext.getBean(beanName);
            throw new IllegalArgumentException(String.format("Delegate %s already exists", beanName));
        } catch (NoSuchBeanDefinitionException ex) {
            applicationContext.registerBean(beanName, TemplateDelegate.class);
            postInitTemplateDelegate(invocation, processKey, variables, beanName);
        }
    }

    /**
     * Initializing delegate variables after it has been created.
     *
     * @param invocation {@link io.github.flexibletech.camunda.tools.delegate.Invocation}
     * @param processKey The key with which the process will be launched in camunda.
     * @param variables  Process variables.
     * @param beanName   The name of the bean to be used for the delegate.
     */
    private void postInitTemplateDelegate(Invocation invocation, String processKey,
                                          Map<String, String> variables, String beanName) {
        TemplateDelegate templateDelegate = applicationContext.getBean(beanName, TemplateDelegate.class);
        templateDelegate.setInvocation(invocation);
        templateDelegate.setProcessKey(processKey);
        templateDelegate.setExpressionVariables(variables);
    }
}
