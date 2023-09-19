package com.example.server.service;

import com.example.server.models.Student;
import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import java.util.List;
@Service
public class ResponseService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ResponseService.class);

    @Autowired
    private Socket zmqResponseSocket;

    @Autowired
    private Socket zmqPublisherSocket;


    @Autowired
    private QueueService queueService;

    private volatile boolean keepRunning = true;



    @Override
    public void run() {
        handleClientRequest();
    }

    /**
     * Broadcasts the current state of the queue to all connected clients.
     * @param queue
     */
    public void broadcastQueue(List<Student> queue) {
        zmqPublisherSocket.sendMore("queue");
        zmqPublisherSocket.send(convertQueueToJson(queue));
        logger.info("Queue updated. Current queue: {}", queue);

    }


    @PostConstruct
    public void startBroadcastingThread() {
        new Thread(() -> {
            while (keepRunning) {
                try {
                    Thread.sleep(5000);  // Dunno if we would have break between checks but without this it will make the PC very slow
                    broadcastQueue(queueService.getQueue());
                } catch (InterruptedException e) {
                    logger.error("Broadcasting thread interrupted", e);
                }
            }
        }).start();
    }


    /**
     *
     * @param queue, list of students in the queue.
     * @return a JSON string of the current queue
     */

    private String convertQueueToJson(List<Student> queue) {
        JSONArray jsonArray = new JSONArray();
        for (Student student : queue) {
            JSONObject studentJson = new JSONObject();
            studentJson.put("name", student.getName());
            studentJson.put("clientIds", student.getClientIds());
            jsonArray.put(studentJson);
        }
        return jsonArray.toString();
    }


    /**
     * listens for and processes client requests.
     */
    public void handleClientRequest() {
        while (keepRunning) {
            String clientRequest = zmqResponseSocket.recvStr();

            // Convert the received string to a JSON object
            JSONObject jsonRequest = new JSONObject(clientRequest);

            // startup message
            if ("startup".equals(jsonRequest.optString("type"))) {
                handleStartupMessage(jsonRequest);
                continue;
            }



            // regular client request
            String response = processClientRequest(clientRequest);
            logger.info("Sending response to client: {}", response);

            zmqResponseSocket.send(response);
            broadcastQueue(queueService.getQueue());
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


    /**
     *
     * @param request The client's request in JSON format.
     * @return A JSON string containing the client's ticket number and name.
     */

    private String processClientRequest(String request) {
        try {
            JSONObject json = new JSONObject(request);
            String name = json.getString("name");
            String clientId = json.getString("clientId");

            queueService.manageStudent(name, clientId);
            int ticket = queueService.getTicket();

            // Create a response JSON with the ticket number and name for the client
            JSONObject responseJson = new JSONObject();
            responseJson.put("ticket", ticket);
            responseJson.put("name", name);

            logger.info("Processed client request. Current queue: {}", queueService.getQueue());
            return responseJson.toString();
            
        } catch (JSONException e) {
            logger.error("Error parsing client request.", e);
                                return "bad response";
        }
    }


    public void stop() {
        this.keepRunning = false;
    }
}
