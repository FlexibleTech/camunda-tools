package io.github.flexibletech.camunda.tools.values.beans;

import io.github.flexibletech.camunda.tools.process.ProcessKeyValue;
import io.github.flexibletech.camunda.tools.process.StartProcess;
import io.github.flexibletech.camunda.tools.task.UserTask;
import io.github.flexibletech.camunda.tools.values.TestApplication;
import io.github.flexibletech.camunda.tools.values.TestValues;
import org.springframework.stereotype.Service;

@Service
public class ApplicationTestService {

    @StartProcess(businessKeyName = TestValues.BUSINESS_KEY_NAME,
            businessKeyValue = "getBusinessKey()",
            processKey = TestValues.PROCESS_KEY)
    public TestApplication testApplication() {
        return new TestApplication(TestValues.BUSINESS_KEY_VALUE);
    }

    @UserTask(definitionKey = TestValues.USER_TASK_FIRST,
            businessKeyValue = TestValues.PROCESS_KEY)
    public TestApplication testUserTask(@ProcessKeyValue String processKey) {
        return new TestApplication(TestValues.BUSINESS_KEY_VALUE);
    }

}
