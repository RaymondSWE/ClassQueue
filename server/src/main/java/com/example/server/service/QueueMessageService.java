package com.example.server.service;

import com.example.server.models.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zeromq.ZMQ;

import java.util.ArrayList;

@Service
public class QueueMessageService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(QueueMessageService.class);
    private final QueueService queueService;

    private final ZMQ.Socket responseSocket;

    public QueueMessageService(ZMQ.Socket responseSocket, QueueService queueService) {
        this.responseSocket = responseSocket;
        this.queueService = queueService;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String msg = new String(responseSocket.recv(), ZMQ.CHARSET);
                System.out.println(msg);
            } catch (Exception e) {
                logger.error("Error receiving message from the client", e);
            }
        }
    }

    private void handleReceivedMessage(String message) {
        String[] parts = message.split(":");
        String action = parts[0];
        String studentName = parts[1];

        if ("ADD".equalsIgnoreCase(action)) {
            queueService.addStudent(new Student(studentName, new ArrayList<>()));
        } else if ("REMOVE".equalsIgnoreCase(action)) {
            queueService.removeStudentByName(studentName);
        }
    }
}