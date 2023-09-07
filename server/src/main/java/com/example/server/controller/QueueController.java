package com.example.server.controller;

import com.example.server.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QueueController {

    @Autowired
    private ZMQ.Socket zmqResponseSocket;

    @Autowired
    private ZMQ.Socket zmqPublisherSocket;

    private List<Users> queue = new ArrayList<>();

    @PostMapping("/joinQueue")
    public String joinQueue(@RequestBody Users user) {
        String message = String.format("{\"enterQueue\":true,\"name\":\"%s\",\"clientId\":\"someUniqueId\"}", user.getUsername());
        zmqResponseSocket.send(message);

        String response = zmqResponseSocket.recvStr();

        queue.add(user);

        // Broadcast queue status
        broadcastQueueStatus();

        return response;
    }

    private void broadcastQueueStatus() {
        String queueStatus = "[";

        for (int i = 0; i < queue.size(); i++) {
            queueStatus += String.format("{\"ticket\": %d, \"name\": \"%s\"}", i, queue.get(i).getUsername());
            if (i < queue.size() - 1) {
                queueStatus += ",";
            }
        }

        queueStatus += "]";

        zmqPublisherSocket.send("queue " + queueStatus);
    }
}
