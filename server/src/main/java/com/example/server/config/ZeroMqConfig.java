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

    @Bean
    public ZMQ.Socket zmqPublisherSocket(ZMQ.Context context) {
        ZMQ.Socket publisher = context.socket(ZMQ.PUB);
        publisher.connect("tcp://ds.iit.his.se:5555");
        return publisher;
    }

    @Bean
    public ZMQ.Socket zmqResponseSocket(ZMQ.Context context) {
        ZMQ.Socket responseSocket = context.socket(ZMQ.REP);
        responseSocket.connect("tcp://ds.iit.his.se:5556");
        return responseSocket;
    }
}
