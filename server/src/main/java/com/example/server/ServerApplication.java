package com.example.server;

import com.example.server.service.ResponseService;
import com.example.server.service.SupervisorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import javax.annotation.PostConstruct;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})

public class ServerApplication {


	@Autowired
	private ResponseService responseService;
@Autowired
private SupervisorService supervisorService;

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@PostConstruct
	public void startSubscriberThread() {

		Thread responseThread = new Thread(responseService);
		responseThread.start();
	}
	@PostConstruct
	public void startSupervisorThread()
	{
		Thread superVisorThread=new Thread(supervisorService);
		superVisorThread.start();

	}
}