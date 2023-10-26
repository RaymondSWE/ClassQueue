package com.example.server.error;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
public class ResponderExceptionHandler {


    private static final Logger logger = LoggerFactory.getLogger(ResponderExceptionHandler.class);
    private Socket zmqResponseSocket;

    public ResponderExceptionHandler(Socket zmqResponseSocket) {
        this.zmqResponseSocket = zmqResponseSocket;
    }

    public void handleErrorMessage(String errorType, String message) {
        JSONObject json = new JSONObject();
        json.put("message", message);
        json.put("error", errorType);
        zmqResponseSocket.send(json.toString());
    }

}
