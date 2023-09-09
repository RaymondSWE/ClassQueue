package com.example.server.config;

import com.example.server.error.SocketBindingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zeromq.ZMQ;

@Configuration
public class TinyQueueApiConfig {

    private static final Logger logger = LoggerFactory.getLogger(TinyQueueApiConfig.class);

    @Bean
    public ZMQ.Socket tinyQueueSubscriberSocket(ZMQ.Context zmqContext) {
        ZMQ.Socket subscriberSocket = zmqContext.socket(ZMQ.SUB);
        try {
            subscriberSocket.connect("tcp://ds.iit.his.se:5555");
            subscriberSocket.subscribe("queue".getBytes());
            logger.info("Subscriber socket connected successfully to tcp://ds.iit.his.se:5555");
        } catch (Exception e) {
            logger.error("Error connecting the subscriber socket to Tinyqueue API", e);
            throw new SocketBindingException("Error connecting the subscriber socket to Tinyqueue API: " + e.getMessage());
        }
        return subscriberSocket;
    }

    @Bean
    public ZMQ.Socket tinyQueueRequesterSocket(ZMQ.Context zmqContext) {
        ZMQ.Socket requesterSocket = zmqContext.socket(ZMQ.REQ);
        try {
            requesterSocket.connect("tcp://ds.iit.his.se:5556");
            logger.info("Requester socket connected successfully to tcp://ds.iit.his.se:5556");
        } catch (Exception e) {
            logger.error("Error connecting the requester socket to Tinyqueue API", e);
            throw new SocketBindingException("Error connecting the requester socket to Tinyqueue API: " + e.getMessage());
        }
        return requesterSocket;
    }
}