package com.example.server.controller;

import com.example.server.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zeromq.ZMQ;

@RestController
public class QueueController {

    @Autowired
    private ZMQ.Socket zmqResponseSocket;

    @PostMapping("/joinQueue")
    public String joinQueue(@RequestBody Users user) {
        String message = String.format("{\"enterQueue\":true,\"name\":\"%s\",\"clientId\":\"someUniqueId\"}", user.getUsername());
        zmqResponseSocket.send(message);

        String response = zmqResponseSocket.recvStr();

        return response;
    }
}
