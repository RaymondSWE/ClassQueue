package com.example.server.service;

import com.example.server.event.NewSupervisorEvent;
import com.example.server.event.SupervisorAssignedStudentEvent;
import com.example.server.event.SupervisorStatusChangedEvent;
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


    public void addSupervisor(String supervisorName) {
        Supervisor supervisor = new Supervisor(supervisorName, SupervisorStatus.AVAILABLE, null, null);
        supervisors.add(supervisor);
        eventPublisher.publishEvent(new NewSupervisorEvent(supervisor));
        logger.info("Supervisor {} connected", supervisorName);
    }

    public List<Supervisor> displayAllConnectedSupervisors() {
        return new ArrayList<>(supervisors);
    }

    public String assignStudentToSupervisor(String supervisorName, String message) {
        Supervisor supervisor = findSupervisorByName(supervisorName);

        if (isSupervisorAvailable(supervisor)) {
            Student student = findFirstStudentInQueue();
            if (student != null) {
                attendToStudent(supervisor, student, message);
                return student.getName();
            } else {
                logger.warn("No students in the queue for");
            }
        } else {
            logger.info("Supervisor not available or not found");
        }
        return "";
    }

    private Supervisor findSupervisorByName(String supervisorName) {
        return supervisors.stream()
                .filter(supervisor -> Objects.equals(supervisor.getName(), supervisorName))
                .findFirst()
                .orElse(null);
    }

    private boolean isSupervisorAvailable(Supervisor supervisor) {
        return supervisor != null && supervisor.getSupervisorStatus() == SupervisorStatus.AVAILABLE;
    }

    private Student findFirstStudentInQueue() {
        return studentService.getQueue().stream().findFirst().orElse(null);
    }

    private void attendToStudent(Supervisor supervisor, Student student, String message) {
        supervisor.setSupervisorStatus(SupervisorStatus.OCCUPIED);
        supervisor.setAttendingStudent(student);
        supervisor.setMessageFromSupervisor(message);
        studentService.removeStudentByName(student.getName());
        eventPublisher.publishEvent(new SupervisorAssignedStudentEvent(this, supervisor.getName(), student.getName(), message));
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

            eventPublisher.publishEvent(new SupervisorStatusChangedEvent(this, supervisorName));

        } else {
            logger.error("Supervisor not found");
        }
    }

}
