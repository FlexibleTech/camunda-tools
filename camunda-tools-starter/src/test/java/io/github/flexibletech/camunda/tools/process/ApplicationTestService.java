package io.github.flexibletech.camunda.tools.process;

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
}
