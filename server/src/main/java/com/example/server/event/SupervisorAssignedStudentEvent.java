package com.example.server.event;

import org.springframework.context.ApplicationEvent;

public class SupervisorAssignedStudentEvent extends ApplicationEvent {
    private final String supervisorName;
    private final String studentName;
    private final String message;

    public SupervisorAssignedStudentEvent(Object source, String supervisorName, String studentName, String message) {
        super(source);
        this.supervisorName = supervisorName;
        this.studentName = studentName;
        this.message = message;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getMessage() {
        return message;
    }
}