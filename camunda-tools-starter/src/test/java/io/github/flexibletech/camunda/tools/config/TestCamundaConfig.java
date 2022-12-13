package io.github.flexibletech.camunda.tools.config;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestCamundaConfig {

    @Bean
    public RuntimeService runtimeService() {
        return BpmnAwareTests.runtimeService();
    }

    @Bean
    public TaskService taskService() {
        return BpmnAwareTests.taskService();
    }
}
