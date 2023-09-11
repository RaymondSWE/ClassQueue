package com.example.server.service;

import com.example.server.models.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zeromq.ZMQ;

import java.util.ArrayList;

@Service
public class QueueMessageService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(QueueMessageService.class);
    private final QueueService queueService;
    private final ZMQ.Socket responseSocket;
    private final ZMQ.Socket publisherSocket;
    private final String topic="queue";
@Autowired
    public QueueMessageService(ZMQ.Socket responseSocket, QueueService queueService, ZMQ.Socket publisherSocket) {
        this.responseSocket = responseSocket;
        this.queueService = queueService;
        this.publisherSocket=publisherSocket;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String msg = new String(responseSocket.recv(), ZMQ.CHARSET);
                logger.info(msg);
                responseSocket.send("b");
            } catch (Exception e) {
//                logger.error("Error receiving message from the client", e);
            }
        }
    }

    private void handleReceivedMessage(String message) {
        String[] parts = message.split(":");
        String action = parts[0];
        String studentName = parts[1];
        String json="{\"ticket\": %d, \"name\": \"%s\"}";
        if ("ADD".equalsIgnoreCase(action)) {
            queueService.addStudent(new Student(studentName, new ArrayList<>()));
            int index=queueService.getQueue().size()-1;
            String notification=String.format(json, index, studentName);
            publisherSocket.sendMore(topic);
            publisherSocket.send(notification);
        } else if ("REMOVE".equalsIgnoreCase(action)) {
            queueService.removeStudentByName(studentName);
        }
    }
}