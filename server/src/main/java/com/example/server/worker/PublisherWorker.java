package com.example.server.worker;

import com.example.server.models.Student;
import com.example.server.service.QueueService;
import com.example.server.service.SupervisorService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ.Socket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PublisherWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(PublisherWorker.class);

    private final Socket zmqPublisherSocket;
    private final QueueService queueService;

    @Autowired
    private SupervisorService supervisorService;

    private volatile boolean keepRunning = true;

    @Autowired
    public PublisherWorker(Socket zmqPublisherSocket, QueueService queueService) {
        this.zmqPublisherSocket = zmqPublisherSocket;
        this.queueService = queueService;
    }

    @Override
    public void run() {
        while (keepRunning) {
            try {
                Thread.sleep(5000);
                List<Student> queue = queueService.getQueue();
                broadcastQueue(queue);
                scheduleBroadcastSupervisorStatus ();
            } catch (InterruptedException e) {
                logger.error("Broadcasting thread interrupted", e);
            }
        }
    }

    private void scheduleBroadcastSupervisorStatus () {
        // Call the SupervisorService's method to get the status and then broadcast it
        List<JSONObject> supervisorStatus = supervisorService.displayAllConnectedSupervisors().stream()
                .map(supervisor -> {
                    JSONObject json = new JSONObject();
                    json.put("name", supervisor.getName());
                    json.put("status", supervisor.getSupervisorStatus().toString().toLowerCase());
                    json.put("client", supervisor.getAttendingStudent() != null ?
                            supervisor.getAttendingStudent().getName() : null);
                    return json;
                }).collect(Collectors.toList());
        zmqPublisherSocket.sendMore("supervisors");
        zmqPublisherSocket.send(supervisorStatus.toString());
        logger.info("Broadcasting supervisors status: {}", supervisorStatus.toString());
    }

    public void broadcastQueue(List<Student> queue) {
        zmqPublisherSocket.sendMore("queue");
        zmqPublisherSocket.send(convertQueueToJson(queue));
        logger.info("Queue updated. Current queue: {}", queue);
    }

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

    public void stop() {
        this.keepRunning = false;
    }
}
