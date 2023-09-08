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
    publisher.bind("tcp://*:5555");
        return publisher;
    }

    @Bean
    public ZMQ.Socket zmqResponseSocket(ZMQ.Context context) {
        ZMQ.Socket responseSocket = context.socket(ZMQ.REP);
        responseSocket.bind("tcp://*:5556");
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    byte[] reply = responseSocket.recv(0);
                    System.out.println("Received: [" + new String(reply, ZMQ.CHARSET) + "]");
    
                    // Do some 'work' here
                    Thread.sleep(1000);
    
                    String response = "world";
                    responseSocket.send(response.getBytes(ZMQ.CHARSET), 0);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
        return responseSocket;
    }
}
