package com.example.server.service;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zeromq.ZMQ.Socket;

import com.example.server.models.Supervisor;


public class SupervisorService {
    private static final org.slf4j.Logger logger=LoggerFactory.getLogger(SupervisorService.class);
@Autowired
private QueueService queueService;
@Autowired
private Socket zmqPublisherSocket;
@Autowired
private Socket zmqResponseSocket;
private List<Supervisor> supervisors=new ArrayList<>();
}
