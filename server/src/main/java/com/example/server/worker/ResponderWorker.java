package com.example.server.worker;

import com.example.server.models.Student;
import com.example.server.service.QueueService;
import com.example.server.service.SupervisorService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ.Socket;

import jakarta.annotation.PostConstruct;

import java.util.List;

@Component
public class ResponderWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ResponderWorker.class);

    @Autowired
    private Socket zmqResponseSocket;

    @Autowired
    private QueueService queueService;

    @Autowired
    private SupervisorService supervisorService;

    private volatile boolean keepRunning = true;

    @Override
    public void run() {
        handleClientRequest();
    }

    @PostConstruct
    public void start() {
        new Thread(this).start();
    }

    public void handleClientRequest() {
        while (keepRunning) {
            String clientRequest = zmqResponseSocket.recvStr();
            JSONObject jsonRequest = new JSONObject(clientRequest);

            if ("startup".equals(jsonRequest.optString("type"))) {
                handleStartupMessage(jsonRequest);
            } else if (jsonRequest.optString("type").equals("supervisor")) {
                String supervisorResponse = supervisorService.processSupervisorRequest(clientRequest);
                logger.info("sending response to supervisor", supervisorResponse);
                zmqResponseSocket.send(supervisorResponse);
            } else {
                // regular client request
                String response = processClientRequest(clientRequest);
                logger.info("Sending response to client: {}", response);
                zmqResponseSocket.send(response);
                // assuming there is a broadcastQueue method similar to the one in ResponseService
                broadcastQueue(queueService.getQueue());
            }
        }
    }

    private void handleStartupMessage(JSONObject jsonRequest) {
        int clientNumber = jsonRequest.optInt("client_number", -1);
        if (clientNumber != -1) {
            logger.info("Received startup message from client number: {}  ", clientNumber);
        } else {
            logger.info("Received startup message from client.");
        }
        zmqResponseSocket.send("Acknowledged startup");
    }

    private String processClientRequest(String request) {
        try {
            JSONObject json = new JSONObject(request);
            String name = json.getString("name");
            String clientId = json.getString("clientId");

            queueService.manageStudent(name, clientId);
            int ticket = queueService.getTicket();

            JSONObject responseJson = new JSONObject();
            responseJson.put("ticket", ticket);
            responseJson.put("name", name);

            logger.info("Processed client request. Current queue: {}", queueService.getQueue());
            return responseJson.toString();

        } catch (Exception e) {
            logger.error("Error parsing client request.", e);
            return "bad response";
        }
    }


    private void broadcastQueue(List<Student> queue) {
        // Yet to be implemented

    }

    public void stop() {
        this.keepRunning = false;
    }
}
