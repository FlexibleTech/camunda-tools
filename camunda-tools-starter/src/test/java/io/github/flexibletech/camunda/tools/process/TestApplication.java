package io.github.flexibletech.camunda.tools.process;

public class TestApplication {
    private final String businessKey;

    TestApplication(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

}
