package io.github.flexibletech.camunda.tools.service.task.core;

import io.github.flexibletech.camunda.tools.delegate.Delegate;
import io.github.flexibletech.camunda.tools.process.ProcessKeyValue;
import io.github.flexibletech.camunda.tools.process.ProcessVariable;
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
        flowEntity.setStatus(FlowEntity.Status.STATUS_2);

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

    @Transactional
    @UserTask(definitionKey = ProcessValues.STEP_4,
            variables = {
                    @ProcessVariable(
                            name = ProcessValues.STATUS,
                            value = "getStatus().name()"
                    )
            })
    public FlowEntity step4(@ProcessKeyValue String id) {
        var flowEntity = cache.get(id);
        flowEntity.setStatus(FlowEntity.Status.STATUS_3);

        System.out.println("Step 4 has been completed.");

        return flowEntity;
    }

    @Transactional
    @UserTask(definitionKey = ProcessValues.STEP_5)
    public void step5(FlowEntity.Status status, @ProcessKeyValue String id) {
        var flowEntity = cache.get(id);
        flowEntity.setStatus(status);

        System.out.println("Step 5 has been completed.");
    }

    @Transactional
    @ReceiveTask(definitionKey = ProcessValues.STEP_6)
    public FlowEntity step6(FlowEntity.Status status, @ProcessKeyValue String id) {
        var flowEntity = cache.get(id);
        flowEntity.setStatus(status);

        System.out.println("Step 5 has been completed.");

        return flowEntity;
    }

}
