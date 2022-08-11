package io.github.flexibletech.camunda.tools.service.task.core;

import java.util.Objects;

public class FlowEntity {
    static final String ID = "1";

    private final String id;
    private Status status;

    private FlowEntity(String id, Status status) {
        this.status = status;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public static FlowEntity newFlowEntity() {
        return new FlowEntity(ID, Status.STATUS_1);
    }

    public enum Status {
        STATUS_1,
        STATUS_2,
        STATUS_3
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
