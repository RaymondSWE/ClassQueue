package com.example.server.config;

import com.example.server.error.SocketBindingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zeromq.ZMQ;

@Configuration
public class ZeroMqConfig {

    private static final Logger logger = LoggerFactory.getLogger(ZeroMqConfig.class);

    @Bean
    public ZMQ.Context zmqContext() {
        return ZMQ.context(1);
    }


    // This zmqPublisherSocket
    // broadcasts messages to all connected clients.
    @Bean
    public ZMQ.Socket zmqPublisherSocket(ZMQ.Context context) {
        ZMQ.Socket publisherSocket = context.socket(SocketType.PUB);
        try {
            publisherSocket.bind("tcp://localhost:5500");
            logger.info("Publisher socket bound successfully to tcp://localhost:5500");
        } catch (Exception e) {
            logger.error("Error binding the publisher socket", e);
            throw new SocketBindingException("Error binding the publisher socket: " + e.getMessage());
        }
        return publisherSocket;
    }
    // This zmqResponseSocket listens for client requests and sends back individual replies.
    @Bean
    public ZMQ.Socket zmqResponseSocket(ZMQ.Context context) {
        ZMQ.Socket responseSocket = context.socket(SocketType.REP);
        try {
            responseSocket.bind("tcp://localhost:5600");
            logger.info("Response socket bound successfully to tcp://localhost:5600");
        } catch (Exception e) {
            logger.error("Error binding the response socket", e);
            throw new SocketBindingException("Error binding the response socket: " + e.getMessage());
        }
        return responseSocket;
    }

}