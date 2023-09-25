package com.example.server.event;

import org.springframework.context.ApplicationEvent;

public class StudentDeletedEvent extends ApplicationEvent {
    private final String studentName;

    public StudentDeletedEvent(Object source, String studentName) {
        super(source);
        this.studentName = studentName;
    }

    public String getStudentName() {
        return studentName;
    }
}
