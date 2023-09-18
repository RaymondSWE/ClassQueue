package com.example.server.models;

import java.util.Objects;

public class Supervisor {
    private String name;
    private String status; // should be able to change from, pending, available and busy
    private Student attendingStudent;
    private String messageFromSupervisor;


    public Supervisor(String name, String status, Student attendingStudent, String messageFromSupervisor) {
        this.name = name;
        this.status = status;
        this.attendingStudent = attendingStudent;
        this.messageFromSupervisor = messageFromSupervisor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Student getAttendingStudent() {
        return attendingStudent;
    }

    public void setAttendingStudent(Student attendingStudent) {
        this.attendingStudent = attendingStudent;
    }

    public String getMessageFromSupervisor() {
        return messageFromSupervisor;
    }

    public void setMessageFromSupervisor(String messageFromSupervisor) {
        this.messageFromSupervisor = messageFromSupervisor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supervisor that = (Supervisor) o;
        return Objects.equals(name, that.name) && Objects.equals(status, that.status) && Objects.equals(attendingStudent, that.attendingStudent) && Objects.equals(messageFromSupervisor, that.messageFromSupervisor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status, attendingStudent, messageFromSupervisor);
    }

    @Override
    public String toString() {
        return "Supervisor{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", attendingStudent=" + attendingStudent +
                ", messageFromSupervisor='" + messageFromSupervisor + '\'' +
                '}';
    }
}
