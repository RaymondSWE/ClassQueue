package com.example.server.service;

import com.example.server.models.Student;
import com.example.server.models.Supervisor;
import com.example.server.models.SupervisorStatus;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zeromq.ZMQ;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SupervisorService implements Runnable {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SupervisorService.class);

    @Autowired
    private QueueService queueService;
    @Autowired
    private ZMQ.Socket zmqPublisherSocket;

    private final List<Supervisor> supervisors = new ArrayList<>();

    private volatile boolean keepRunning = true;

    // Method to add a new supervisor
    public void addSupervisor(String supervisorName) {
        Supervisor supervisor = new Supervisor(supervisorName, SupervisorStatus.AVAILABLE, null, null);
        supervisors.add(supervisor);
        logger.info("Supervisor {} connected", supervisorName);
        broadcastSupervisorsStatus();
    }


    // Display info about all supervisors currently connected
    public List<Supervisor> displayAllConnectedSupervisors() {
        return new ArrayList<>(supervisors);
    }

    // Attend to the first student in the queue
    public void attendStudent(String supervisorName, String message) {
        Supervisor supervisor = supervisors.stream()
                .filter(s -> Objects.equals(s.getName(), supervisorName))
                .findFirst()
                .orElse(null);

        if (supervisor != null && supervisor.getSupervisorStatus() == SupervisorStatus.AVAILABLE) {
            Student student = queueService.removeFirstStudent();
            if (student != null) {
                supervisor.setSupervisorStatus(SupervisorStatus.BUSY);
                supervisor.setAttendingStudent(student);
                supervisor.setMessageFromSupervisor(message);
                sendUserMessage(supervisorName, student.getName(), message);
                broadcastSupervisorsStatus();
            } else {
                logger.info("No students in the queue");
            }
        } else {
            logger.info("Supervisor not available or not found");
        }
    }

    // Send a message to a specific user
    private void sendUserMessage(String supervisorName, String userName, String message) {
        JSONObject json = new JSONObject();
        json.put("supervisor", supervisorName);
        json.put("message", message);
        zmqPublisherSocket.sendMore(userName);
        zmqPublisherSocket.send(json.toString());
        logger.info("Sending user message to {}: {}", userName, json.toString());
    }

    // Broadcast the status of all connected supervisors
    private void broadcastSupervisorsStatus() {
        List<JSONObject> supervisorsStatus = supervisors.stream().map(supervisor -> {
            JSONObject json = new JSONObject();
            json.put("name", supervisor.getName());
            json.put("status", supervisor.getSupervisorStatus().toString().toLowerCase());
            json.put("client", supervisor.getAttendingStudent() != null ?
                    supervisor.getAttendingStudent().getName() : null);
            return json;
        }).collect(Collectors.toList());
        zmqPublisherSocket.sendMore("supervisors");
        zmqPublisherSocket.send(supervisorsStatus.toString());
        logger.info("Broadcasting supervisors status: {}", supervisorsStatus.toString());
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
    }
}
