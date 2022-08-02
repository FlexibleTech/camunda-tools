package io.github.flexibletech.camunda.tools.delegate;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class DelegateBeanPostProcessor implements BeanPostProcessor {
    private final DelegatesBeanRegistrar delegatesBeanRegistrar;

    public DelegateBeanPostProcessor(DelegatesBeanRegistrar delegatesBeanRegistrar) {
        this.delegatesBeanRegistrar = delegatesBeanRegistrar;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        delegatesBeanRegistrar.registerDelegatesForBean(bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
