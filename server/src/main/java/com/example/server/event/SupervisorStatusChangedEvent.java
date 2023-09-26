package com.example.server.event;

import org.springframework.context.ApplicationEvent;

public class SupervisorStatusChangedEvent extends ApplicationEvent {
    private final String supervisorName;

    public SupervisorStatusChangedEvent(Object source, String supervisorName) {
        super(source);
        this.supervisorName = supervisorName;
    }

    public String getSupervisorName() {
        return supervisorName;
    }
}