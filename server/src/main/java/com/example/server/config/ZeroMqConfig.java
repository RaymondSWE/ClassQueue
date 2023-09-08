package com.example.server.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zeromq.ZMQ;

@Configuration
public class ZeroMqConfig {


    @Bean
    public ZMQ.Context zmqContext() {
        return ZMQ.context(1);
    }

    // Publisher to broadcast messages to clients
    @Bean
    public ZMQ.Socket zmqPublisherSocket(ZMQ.Context context) {
        ZMQ.Socket publisher = context.socket(ZMQ.PUB);
    publisher.bind("tcp://*:5500");
        return publisher;
    }
    // Res for responding to each client request
    @Bean
    public ZMQ.Socket zmqResponseSocket(ZMQ.Context context) {
        ZMQ.Socket responseSocket = context.socket(ZMQ.REP);
        responseSocket.bind("tcp://*:5600");
        return responseSocket;
    }
}
