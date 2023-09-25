package com.example.server.event;

import org.springframework.context.ApplicationEvent;

public class NewSupervisorEvent extends ApplicationEvent {
    public NewSupervisorEvent(Object source) {
        super(source);
    }
}
