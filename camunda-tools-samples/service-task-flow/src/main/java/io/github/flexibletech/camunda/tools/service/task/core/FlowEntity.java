package io.github.flexibletech.camunda.tools.service.task.core;

import java.util.Objects;

public class FlowEntity {
    static final int OK_STATUS = 1;
    static final int REJECT_STATUS = 0;
    static final String ID = "1";

    private final String id;
    private Integer status;

    private FlowEntity(String id, Integer status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static FlowEntity newFlowEntity() {
        return new FlowEntity(ID, OK_STATUS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlowEntity that = (FlowEntity) o;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }
}
