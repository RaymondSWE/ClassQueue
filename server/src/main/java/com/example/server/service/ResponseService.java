package com.example.server.service;

import com.example.server.models.Student;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zeromq.ZMQ.Socket;

import java.util.ArrayList;
import java.util.List;
@Service
public class ResponseService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ResponseService.class);

    @Autowired
    private Socket zmqResponseSocket;

    @Autowired
    private QueueService queueService;

    private volatile boolean keepRunning = true;

    @Override
    public void run() {
        handleClientRequest();
    }

    public void handleClientRequest() {
        while (keepRunning) {
            String clientRequest = zmqResponseSocket.recvStr();
            logger.info("Received request from client: {}", clientRequest);

            String response = processClientRequest(clientRequest);
            logger.info("Sending response to client: {}", response);

            zmqResponseSocket.send(response);
        }
    }

    private String processClientRequest(String request) {
        String response="bad response";
        try {
            JSONObject json = new JSONObject(request);
            String name = json.getString("name");
            String clientId = json.getString("clientId");

            queueService.manageStudent(name, clientId);
            int ticket=queueService.getTicket();
            //get the ticket of the last user added to the queue
            JSONObject responseJson=new JSONObject();            
            responseJson.put("ticket", ticket);
                        responseJson.put("name", name);
                         response=responseJson.toString();
            logger.info("Current queue: {}", queueService.getQueue());
        } catch (JSONException e) {
            logger.error("Error parsing client request.", e);
        }
        return response;
    }

    public void stop() {
        this.keepRunning = false;
    }
}
