package com.example.server.service;

import com.example.server.event.NewSupervisorEvent;
import com.example.server.models.Student;
import com.example.server.models.Supervisor;
import com.example.server.models.SupervisorStatus;
import com.example.server.worker.PublisherWorker;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SupervisorService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SupervisorService.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    @Lazy
    private PublisherWorker publisherWorker;

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public SupervisorService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    private final List<Supervisor> supervisors = new ArrayList<>();


    // Method to add a new supervisor
    public void addSupervisor(String supervisorName) {
        Supervisor supervisor = new Supervisor(supervisorName, SupervisorStatus.AVAILABLE, null, null);
        supervisors.add(supervisor);
        eventPublisher.publishEvent(new NewSupervisorEvent(supervisor));
        logger.info("Supervisor {} connected", supervisorName);
    }


    // Display info about all supervisors currently connected
    public List<Supervisor> displayAllConnectedSupervisors() {
        return new ArrayList<>(supervisors);
    }

    // Attend to the first student in the queue
    public String attendStudent(String supervisorName, String message) {
        Supervisor supervisor = supervisors.stream()
                .filter(s -> Objects.equals(s.getName(), supervisorName))
                .findFirst()
                .orElse(null);

        if (supervisor != null && supervisor.getSupervisorStatus() == SupervisorStatus.AVAILABLE) {
            Student student = studentService.getQueue().stream()
                    .findFirst()
                    .orElse(null);
            if (student != null) {
                supervisor.setSupervisorStatus(SupervisorStatus.OCCUPIED);
                supervisor.setAttendingStudent(student);
                supervisor.setMessageFromSupervisor(message);
                publisherWorker.sendStudentMessage(supervisorName, student.getName(), message);
                studentService.removeStudentByName(student.getName());

                publisherWorker.broadcastQueue(studentService.getQueue());
                publisherWorker.broadcastSupervisorsStatus();
                logger.info("Student {} is being attended by Supervisor {}", student.getName(), supervisorName);
                return student.getName();
            } else {
                logger.warn("No students in the queue for");
            }
        } else {
            logger.info("Supervisor not available or not found");
        }
        return "";
    }


    public void makeSupervisorAvailable(String supervisorName) {
        Supervisor supervisor = supervisors.stream()
                .filter(s -> Objects.equals(s.getName(), supervisorName))
                .findFirst()
                .orElse(null);

        if (supervisor != null) {
            supervisor.setSupervisorStatus(SupervisorStatus.AVAILABLE);
            supervisor.setAttendingStudent(null);
            supervisor.setMessageFromSupervisor(null);

            publisherWorker.broadcastSupervisorsStatus();
            publisherWorker.broadcastQueue(studentService.getQueue());
            logger.info("Supervisor {} is now available", supervisorName);
        } else {
            logger.error("Supervisor not found");
        }
    }

}
