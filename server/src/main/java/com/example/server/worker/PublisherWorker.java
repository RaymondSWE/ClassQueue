package com.example.server.worker;

import com.example.server.models.Student;
import com.example.server.service.StudentService;
import com.example.server.service.SupervisorService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ.Socket;

import java.util.List;

@Component
public class PublisherWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(PublisherWorker.class);

    private final Socket zmqPublisherSocket;
    private final StudentService studentService;

    @Autowired
    private SupervisorService supervisorService;

    private volatile boolean keepRunning = true;

    @Autowired
    public PublisherWorker(Socket zmqPublisherSocket, StudentService studentService) {
        this.zmqPublisherSocket = zmqPublisherSocket;
        this.studentService = studentService;
    }

    @Override
    public void run() {
        while (keepRunning) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                logger.error("Broadcasting thread interrupted", e);
            }
        }
    }



    public void broadcastQueue(List<Student> queue) {
        zmqPublisherSocket.sendMore("queue");
        zmqPublisherSocket.send(convertQueueToJson(queue));
        logger.info("Queue updated. Current student queue: {}", queue);
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


    public void sendUserMessage(String supervisorName, String userName, String message) {
        JSONObject json = new JSONObject();
        json.put("supervisor", supervisorName);
        json.put("message", message);
        zmqPublisherSocket.sendMore(userName);
        zmqPublisherSocket.send(json.toString());
        logger.info("Sending user message to {}: {}", userName, json.toString());
    }

    public void broadcastSupervisorsStatus() {
        List<JSONObject> supervisorsStatus = supervisorService.displayAllConnectedSupervisors().stream().map(supervisor -> {
            JSONObject json = new JSONObject();
            json.put("name", supervisor.getName());
            json.put("status", supervisor.getSupervisorStatus().toString().toLowerCase());
            json.put("client", supervisor.getAttendingStudent() != null ?
                    supervisor.getAttendingStudent().getName() : null);
            return json;
        }).toList();
        zmqPublisherSocket.sendMore("supervisors");
        zmqPublisherSocket.send(supervisorsStatus.toString());
        logger.info("Broadcasting supervisors status: {}", supervisorsStatus.toString());
    }

}
