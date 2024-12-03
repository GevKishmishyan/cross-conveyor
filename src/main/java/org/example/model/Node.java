package org.example.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Node {
    private Integer value;
    private final Set<String> conveyors = new HashSet<>();
    private boolean isShared;

    public Node() {}

    public Node(int value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void addConveyor(String conveyorId) {
        conveyors.add(conveyorId);
    }

    public void addConveyors(Set<String> conveyorIds) {
        conveyors.addAll(conveyorIds);
    }

    public Set<String> getConveyors() {
        return conveyors;
    }

    public void setShared(boolean isShared) {
        this.isShared = isShared;
    }

    public boolean isShared() {
        return isShared;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return isShared == node.isShared && Objects.equals(value, node.value) && Objects.equals(conveyors, node.conveyors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, conveyors, isShared);
    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                ", conveyors=" + conveyors +
                ", isShared=" + isShared +
                '}';
    }
}
