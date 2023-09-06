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
    public ZMQ.Socket zmqSubscriberSocket(ZMQ.Context context) {
        ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
        subscriber.connect("tcp://ds.iit.his.se:5555");
        subscriber.subscribe(""); // Empty string to receive all messages.
        return subscriber;
    }

    @Bean
    public ZMQ.Socket zmqRequestSocket(ZMQ.Context context) {
        ZMQ.Socket requester = context.socket(ZMQ.REQ);
        requester.connect("tcp://ds.iit.his.se:5556");
        return requester;
    }
}
