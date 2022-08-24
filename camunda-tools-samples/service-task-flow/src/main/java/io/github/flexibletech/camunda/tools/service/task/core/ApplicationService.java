package io.github.flexibletech.camunda.tools.service.task.core;

import io.github.flexibletech.camunda.tools.delegate.Delegate;
import io.github.flexibletech.camunda.tools.delegate.Delegates;
import io.github.flexibletech.camunda.tools.process.values.ProcessKeyValue;
import io.github.flexibletech.camunda.tools.process.values.ProcessValue;
import io.github.flexibletech.camunda.tools.process.values.ProcessValues;
import io.github.flexibletech.camunda.tools.process.variables.ProcessVariable;
import io.github.flexibletech.camunda.tools.process.start.StartProcess;
import io.github.flexibletech.camunda.tools.task.receive.ReceiveTask;
import io.github.flexibletech.camunda.tools.task.user.UserTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class ApplicationService {
    private final Map<String, FlowEntity> cache = new HashMap<>();
    private final String processKey = "processKey";

    @Transactional
    @StartProcess(businessKeyName = ProcessConstants.BUSINESS_KEY, businessKeyValue = "getId()", processKey = processKey)
    public FlowEntity startProcess() {
        var flowEntity = FlowEntity.newFlowEntity();
        cache.put(flowEntity.getId(), flowEntity);
        System.out.println("Flow entity has been created.");

        return flowEntity;
    }

    @Delegate(beanName = ProcessConstants.STEP_1, key = ProcessConstants.BUSINESS_KEY)
    public FlowEntity step1(@ProcessKeyValue String id) {
        var flowEntity = cache.get(id);
        flowEntity.setStatus(FlowEntity.Status.STATUS_2);

        System.out.println("Step 1 has been completed.");

        return flowEntity;
    }

    @Delegate(beanName = ProcessConstants.STEP_2, key = ProcessConstants.BUSINESS_KEY)
    public void step2(@ProcessKeyValue String id) {
        System.out.println("Step 2 has been completed.");
    }

    @Delegate(beanName = ProcessConstants.STEP_3, key = ProcessConstants.BUSINESS_KEY)
    public void step3(@ProcessKeyValue String id) {
        System.out.println("Step 3 has been completed.");
    }

    @Transactional
    @UserTask(definitionKey = ProcessConstants.STEP_4,
            variables = {@ProcessVariable(name = ProcessConstants.STATUS, value = "getStatus().name()")})
    public FlowEntity step4(@ProcessKeyValue String id) {
        var flowEntity = cache.get(id);
        flowEntity.setStatus(FlowEntity.Status.STATUS_3);

        System.out.println("Step 4 has been completed.");

        return flowEntity;
    }

    @Transactional
    @UserTask(definitionKey = ProcessConstants.STEP_5)
    public void step5(FlowEntity.Status status, @ProcessKeyValue String id) {
        var flowEntity = cache.get(id);
        flowEntity.setStatus(status);

        System.out.println("Step 5 has been completed.");
    }

    @Transactional
    @ReceiveTask(definitionKey = ProcessConstants.STEP_6)
    public FlowEntity step6(FlowEntity.Status status, @ProcessKeyValue String id) {
        var flowEntity = cache.get(id);
        flowEntity.setStatus(status);

        System.out.println("Step 5 has been completed.");

        return flowEntity;
    }

    @Delegates(
            values = {
                    @Delegate(beanName = ProcessConstants.STEP_7_1, key = ProcessConstants.BUSINESS_KEY),
                    @Delegate(beanName = ProcessConstants.STEP_7_2, key = ProcessConstants.BUSINESS_KEY),
            }
    )
    public FlowEntity step7(@ProcessValues(values = {
            @ProcessValue(value = "STATUS_5", type = FlowEntity.Status.class, delegate = ProcessConstants.STEP_7_1),
            @ProcessValue(value = "STATUS_6", type = FlowEntity.Status.class, delegate = ProcessConstants.STEP_7_2)
    }) FlowEntity.Status status, @ProcessKeyValue String id) {
        var flowEntity = cache.get(id);
        flowEntity.setStatus(status);

        System.out.printf("Flow entity status %s%n", flowEntity.getStatus());
        System.out.println("Step 7 has been completed.");

        return flowEntity;
    }

}
