package io.github.flexibletech.camunda.tools.service.task.core;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationServiceRunner implements CommandLineRunner {
    private final ApplicationService applicationService;

    public ApplicationServiceRunner(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public void run(String... args) {
        applicationService.startProcess();
    }

}
