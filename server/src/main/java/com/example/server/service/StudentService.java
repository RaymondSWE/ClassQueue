package com.example.server.service;

import com.example.server.event.NewStudentEvent;
import com.example.server.event.StudentDeletedEvent;
import com.example.server.models.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@EnableScheduling
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final List<Student> queue = new ArrayList<>();
    private String name;
    private int ticket = -1;
    private Map<String, Long> lastHeartbeatReceived = new HashMap<>();
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public StudentService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }


    public int getTicket() {
        int index = 0;
        for (Student student : queue) {
            if (student.getName().equals(this.name)) {
                this.ticket = index;
                break;
            }
            index++;
        }

        return ticket;
    }


    public void manageStudent(String name, String clientId) {
        this.name = name;
        Student existingStudent = findStudentByName(name);
        if (existingStudent == null) {
            createAndEnqueueStudent(name, clientId);
        } else if (!existingStudent.getClientIds().contains(clientId)) {
            existingStudent.getClientIds().add(clientId);
        }

    }

    private Student findStudentByName(String name) {
        return  queue.stream()
                .filter(student -> student.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private void createAndEnqueueStudent(String name, String clientId) {
        List<String> clientIds = new ArrayList<>();
        clientIds.add(clientId);
        Student newStudent = new Student(name, clientIds);
        enqueueStudent(newStudent);
        logger.info("New student joined: {}", name);
    }

    private void enqueueStudent(Student student) {
        if (!queue.contains(student)) {
            queue.add(student);
            eventPublisher.publishEvent(new NewStudentEvent(student));
        }
    }

    public void removeStudentByName(String name) {
        queue.removeIf(student -> {
            boolean foundMatchingStudent = student.getName().equals(name);
            if (foundMatchingStudent) {
                eventPublisher.publishEvent(new StudentDeletedEvent(this, name));
            }
            return foundMatchingStudent;
        });
    }


    @Scheduled(fixedRate = 4000)
    public void removeInactiveStudents() {
        Long currentTime = System.currentTimeMillis();
        lastHeartbeatReceived.entrySet().removeIf(entry -> {
            Long elapsedTime = currentTime - entry.getValue();
            boolean isStudentInactive = elapsedTime > 4000;
            if (isStudentInactive) {
                removeStudentByName(entry.getKey());
                logger.warn("Removed inactive student with name: {}", entry.getKey());
                eventPublisher.publishEvent(new StudentDeletedEvent(this, name));
            }
            return isStudentInactive;
        });
    }

    public void updateClientHeartbeat(String clientId) {
        lastHeartbeatReceived.put(clientId, System.currentTimeMillis());
    }

    public List<Student> getQueue() {
        return new ArrayList<>(queue);
    }

}

