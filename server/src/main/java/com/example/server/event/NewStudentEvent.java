package com.example.server.event;

import org.springframework.context.ApplicationEvent;

public class NewStudentEvent extends ApplicationEvent {
    public NewStudentEvent(Object source) {
        super(source);
    }
}
