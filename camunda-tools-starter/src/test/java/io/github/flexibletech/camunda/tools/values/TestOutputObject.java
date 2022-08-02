package io.github.flexibletech.camunda.tools.values;

public class TestOutputObject {
    private final String value;

    public TestOutputObject(String value) {
        this.value = value;
    }

    public String getClassName() {
        return this.getClass().getName();
    }

    public String getValue() {
        return value;
    }
}
