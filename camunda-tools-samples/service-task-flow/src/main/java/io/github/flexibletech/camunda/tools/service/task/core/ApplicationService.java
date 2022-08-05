package io.github.flexibletech.camunda.tools.service.task.core;

import io.github.flexibletech.camunda.tools.delegate.Delegate;
import io.github.flexibletech.camunda.tools.process.ProcessKeyValue;
import io.github.flexibletech.camunda.tools.process.StartProcess;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ApplicationService {
    private final Map<String, FlowEntity> cache = new HashMap<>();
    private final String processKey = "processKey";

    @StartProcess(businessKeyName = ProcessValues.BUSINESS_KEY, businessKeyValue = "getId()", processKey = processKey)
    public FlowEntity startProcess() {
        var flowEntity = FlowEntity.newFlowEntity();
        cache.put(flowEntity.getId(), flowEntity);
        System.out.println("Flow entity has been created.");
        return flowEntity;
    }

    @Delegate(beanName = ProcessValues.STEP_1, key = ProcessValues.BUSINESS_KEY)
    public FlowEntity step1(@ProcessKeyValue String id) {
        var flowEntity = cache.get(id);
        flowEntity.setStatus(FlowEntity.REJECT_STATUS);

        System.out.println("Step 1 has been completed.");

        return flowEntity;
    }

    @Delegate(beanName = ProcessValues.STEP_2, key = ProcessValues.BUSINESS_KEY)
    public void step2(@ProcessKeyValue String id) {
        System.out.println("Step 2 has been completed.");
    }

    @Delegate(beanName = ProcessValues.STEP_3, key = ProcessValues.BUSINESS_KEY)
    public void step3(@ProcessKeyValue String id) {
        System.out.println("Step 3 has been completed.");
    }

}
