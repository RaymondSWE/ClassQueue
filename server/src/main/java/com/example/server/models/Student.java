package com.example.server.models;

import java.util.List;
import java.util.Objects;

public class Student {
    private String name;
    private List<String> clientIds;

    public Student(String name, List<String> clientIds) {
        this.name = name;
        this.clientIds = clientIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getClientIds() {
        return clientIds;
    }

    public void setClientIds(List<String> clientIds) {
        this.clientIds = clientIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(name, student.name) && Objects.equals(clientIds, student.clientIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, clientIds);
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", clientIds=" + clientIds +
                '}';
    }
}
