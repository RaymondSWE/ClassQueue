package com.example.server.event;

import org.springframework.context.ApplicationEvent;

public class StudentDeletedEvent extends ApplicationEvent {

    public StudentDeletedEvent(Object source) {
        super(source);
    }
}
