package com.example.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.zeromq.channel.SubscribableZmqChannel;
import org.springframework.integration.zeromq.inbound.ZmqMessageDrivenChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

@Configuration
public class ZeroMqConfig {

    @Bean
    public SubscribableZmqChannel zmqChannel() {
        return new SubscribableZmqChannel("tcp://ds.iit.his.se:5555", "sub");
    }

    @Bean
    public ZmqMessageDrivenChannelAdapter messageDrivenChannelAdapter() {
        ZmqMessageDrivenChannelAdapter adapter = new ZmqMessageDrivenChannelAdapter(zmqChannel());
        adapter.setOutputChannelName("outputChannel");
        return adapter;
    }

    @Bean
    public MessageHandler outboundAdapter() {
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) throws Exception {
                // Handle incoming message from ZeroMQ subscription here.
                System.out.println("Received: " + message.getPayload());
            }
        };
    }
}