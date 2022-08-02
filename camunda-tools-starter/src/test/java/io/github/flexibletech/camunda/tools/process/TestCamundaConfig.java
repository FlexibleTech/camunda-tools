package io.github.flexibletech.camunda.tools.process;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestCamundaConfig {

    @Bean
    public RuntimeService runtimeService() {
        return BpmnAwareTests.runtimeService();
    }

}
