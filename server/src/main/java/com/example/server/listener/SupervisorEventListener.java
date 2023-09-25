package com.example.server.listener;

import com.example.server.event.NewSupervisorEvent;
import com.example.server.worker.PublisherWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SupervisorEventListener {

    private final PublisherWorker publisherWorker;

    @Autowired
    public SupervisorEventListener(PublisherWorker publisherWorker) {
        this.publisherWorker = publisherWorker;
    }

    @EventListener
    public void handleNewSupervisorEvent(NewSupervisorEvent event) {
        publisherWorker.broadcastSupervisorsStatus();
    }
}
