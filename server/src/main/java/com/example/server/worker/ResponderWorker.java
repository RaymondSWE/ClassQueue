package com.example.server.worker;

import com.example.server.service.StudentService;
import com.example.server.service.SupervisorService;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ.Socket;

@Component
public class ResponderWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ResponderWorker.class);

    @Autowired
    private Socket zmqResponseSocket;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SupervisorService supervisorService;

    @Autowired
    private PublisherWorker publisherWorker;
private String invalidMessageType="invalidMessage";
    private volatile boolean keepRunning = true;

    private void sendErrorMsg(String errorType, String message)
    {
JSONObject json=new JSONObject();
json.put("message", message);
json.put("error", errorType);
zmqResponseSocket.send(json.toString());
    }
    @Override
    public void run() {
        handleClientRequest();
    }

    @PostConstruct
    public void start() {
        new Thread(this).start();
    }

    public void handleClientRequest() {
        while (keepRunning && !Thread.currentThread().isInterrupted()) {
            try {
                String clientRequest = zmqResponseSocket.recvStr();
                JSONObject jsonRequest = new JSONObject(clientRequest);
                String type = jsonRequest.optString("type");
                switch (type) {
                    case "heartbeat":
                        handleHeartbeat(jsonRequest);
                        break;
                    case "supervisor":
                        handleSupervisorRequest(jsonRequest);
                        break;
                    case "startup":
                        handleStartupMessage(jsonRequest);
                        break;
                    default:
                   processClientRequest(jsonRequest);
                        break;
                }
            } catch (Exception e) {
                if (!keepRunning) {
                    break;
                }
                logger.error("Error handling client request: ", e);
            }
        }
    }

    private void handleHeartbeat(JSONObject jsonRequest) {
        if(jsonRequest.has("clientId")&&jsonRequest.has("name"))
        {
        String name = jsonRequest.getString("name");
        String clientId = jsonRequest.getString("clientId");
        logger.info("Received heartbeat from: {} with clientId: {}", name, clientId);
        studentService.updateClientHeartbeat(name);
        zmqResponseSocket.send(new JSONObject().toString()); // empty JSON object as a response
    }
    else
    {
        logger.info("no client id found");
        sendErrorMsg(invalidMessageType, "your message is not valid");
    }
    }


    private void handleStartupMessage(JSONObject jsonRequest) {
        if(jsonRequest.has("client_number"))
        {
        int clientNumber = jsonRequest.optInt("client_number", -1);
        
        if (clientNumber != -1) {
            logger.info("Received startup message from client number: {}  ", clientNumber);
        } else {
            logger.info("Received startup message from client.");
        }
        // Broadcast the current state of the student and supervisor queues
        publisherWorker.broadcastQueue(studentService.getQueue());
        publisherWorker.broadcastSupervisorsStatus();

        zmqResponseSocket.send("Acknowledged startup");
    }
    else
    {
        logger.info("no client id found");
        sendErrorMsg(invalidMessageType, "no client id was found");
    }

}
    //TODO::Add a method to disconnect as supervisor, would be cool.
    // Also really ugly ass code, switch to switch
    private void handleSupervisorRequest(JSONObject jsonRequest) {
        logger.info("Received Supervisor Request: {}", jsonRequest.toString());
        if (jsonRequest.has("addSupervisor")) {
            if(!jsonRequest.has("supervisorName"))
            {
                logger.info("could not find supervisorName in message");
                sendErrorMsg(invalidMessageType, "invalid request. unable to find supervisorName");
                return;
            }
            supervisorService.addSupervisor(jsonRequest.getString("supervisorName"));
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "success");
            jsonResponse.put("message", "Supervisor added successfully");
            zmqResponseSocket.send(jsonResponse.toString());
        } else if (jsonRequest.has("attendStudent")) {
            if(jsonRequest.has("supervisorName")&&jsonRequest.has("message"))
            {
            String studentName = supervisorService.attendStudent(jsonRequest.getString("supervisorName"), jsonRequest.getString("message"));
            if (!studentName.equals("")) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "attending: " + studentName);
                jsonResponse.put("status", "success");
                zmqResponseSocket.send(jsonResponse.toString());
            } else {
                sendErrorMsg(invalidMessageType, "failed to attend students");
        }
            }
    else
    {
logger.info("invalud request. supervisor message and name not found");
sendErrorMsg(invalidMessageType, "invalid request. supervisorName or message is missing");
return;
    
}
        } else if (jsonRequest.has("makeAvailable")) {
            if(!jsonRequest.has("supervisorName"))
            {
                logger.info("invalid request. could not find supervisorName");
                sendErrorMsg(invalidMessageType, "invalid request. could not find supervisorName");
                return;
            }
            String supervisorName = jsonRequest.getString("supervisorName");
            supervisorService.makeSupervisorAvailable(supervisorName);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "success");
            jsonResponse.put("message", "Supervisor is now available");
            zmqResponseSocket.send(jsonResponse.toString());
        } else {
            sendErrorMsg(invalidMessageType, "invalid supervisor request");
        }
    }


    private void processClientRequest(JSONObject json) {
        try {
            String name = json.getString("name");
            String clientId = json.getString("clientId");

            studentService.manageStudent(name, clientId);
            int ticket = studentService.getTicket();

            JSONObject responseJson = new JSONObject();
            responseJson.put("ticket", ticket);
            responseJson.put("name", name);
            zmqResponseSocket.send(responseJson.toString());
        } catch (Exception e) {
            logger.error("Error parsing client request.", e);
            sendErrorMsg(invalidMessageType, "invalid queue request");
        }
    }


    public void stop() {
        this.keepRunning = false;
    }
}
