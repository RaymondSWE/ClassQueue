package com.example.server.event;

import com.example.server.models.Student;
import org.springframework.context.ApplicationEvent;

import org.springframework.context.ApplicationEvent;

public class NewStudentEvent extends ApplicationEvent {
    public NewStudentEvent(Object source) {
        super(source);
    }
}
