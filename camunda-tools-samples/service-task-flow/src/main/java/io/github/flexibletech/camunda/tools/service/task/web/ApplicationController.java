package io.github.flexibletech.camunda.tools.service.task.web;

import io.github.flexibletech.camunda.tools.service.task.core.ApplicationService;
import io.github.flexibletech.camunda.tools.service.task.core.FlowEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/processes")
    public void start() {
        applicationService.startProcess();
    }

    @PostMapping("/processes/{id}/step4")
    public void completeStep4(@PathVariable String id) {
        applicationService.step4(id);
    }

    @PostMapping("/processes/{id}/step5")
    public void completeStep5(@PathVariable String id) {
        applicationService.step5(FlowEntity.Status.STATUS_3, id);
    }

    @PostMapping("/processes/{id}/step6")
    public void completeStep6(@PathVariable String id) {
        applicationService.step6(FlowEntity.Status.STATUS_4, id);
    }

}
