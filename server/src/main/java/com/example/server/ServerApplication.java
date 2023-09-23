package com.example.server;

import com.example.server.worker.PublisherWorker;
import com.example.server.worker.ResponderWorker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import javax.annotation.PostConstruct;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class ServerApplication {

	@Autowired
	private ResponderWorker responderWorker;
	@Autowired
	private PublisherWorker publisherWorker;

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@PostConstruct
	public void startWorkerThreads() {
		Thread responderThread = new Thread(responderWorker);
		responderThread.start();

		Thread publisherThread = new Thread(publisherWorker);
		publisherThread.start();
	}
}
