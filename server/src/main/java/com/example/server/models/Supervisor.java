package com.example.server.models;

import java.util.Objects;

public class Supervisor {
    private String name;
    private SupervisorStatus supervisorStatus; // should be able to change from, pending, available and busy
    private Student attendingStudent;
    private String messageFromSupervisor;


    public Supervisor(String name, SupervisorStatus supervisorStatus, Student attendingStudent, String messageFromSupervisor) {
        this.name = name;
        this.supervisorStatus = supervisorStatus;
        this.attendingStudent = attendingStudent;
        this.messageFromSupervisor = messageFromSupervisor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SupervisorStatus getSupervisorStatus() {
        return supervisorStatus;
    }

    public void setSupervisorStatus(SupervisorStatus supervisorStatus) {
        this.supervisorStatus = supervisorStatus;
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
        return Objects.equals(name, that.name) && supervisorStatus == that.supervisorStatus && Objects.equals(attendingStudent, that.attendingStudent) && Objects.equals(messageFromSupervisor, that.messageFromSupervisor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, supervisorStatus, attendingStudent, messageFromSupervisor);
    }
}
