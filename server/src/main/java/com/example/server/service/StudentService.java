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

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public StudentService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    private Map<String, Long> lastHeartbeatReceived = new HashMap<>();


    public int getTicket() {
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).getName().equals(this.name)) {
                this.ticket = i;
                break;
            }
        }

        return ticket;
    }

    public void manageStudent(String name, String clientId) {
        this.name = name;
        Student existingStudent = queue.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElse(null);
        if (existingStudent == null) {
            List<String> clientIds = new ArrayList<>();
            clientIds.add(clientId);
            Student newStudent = new Student(name, clientIds);
            addStudent(newStudent);
            logger.info("New student joined: {}", name);
        } else if (!existingStudent.getClientIds().contains(clientId)) {
            existingStudent.getClientIds().add(clientId);
        }

    }

    private void addStudent(Student student) {
        if (!queue.contains(student)) {
            queue.add(student);
            eventPublisher.publishEvent(new NewStudentEvent(student));

        }
    }

    public void removeStudentByName(String name) {
        queue.removeIf(student -> {
            if (student.getName().equals(name)) {
                eventPublisher.publishEvent(new StudentDeletedEvent(this, name));
                return true;
            }
            return false;
        });
    }

    public void updateClientHeartbeat(String clientId) {
        lastHeartbeatReceived.put(clientId, System.currentTimeMillis());

    }

    @Scheduled(fixedRate = 4000)
    public void removeInactiveStudents() {
        Long currentTime = System.currentTimeMillis();
        for (Map.Entry<String, Long> entry : lastHeartbeatReceived.entrySet()) {
            Long elapsedTime = currentTime - entry.getValue();
            if (elapsedTime > 4000) {
                removeStudentByName(entry.getKey());
                lastHeartbeatReceived.remove(entry.getKey());
                logger.info("Removed inactive student with name: {}", entry.getKey());
            }
        }
    }


    //remove the first student in queue
    public Student removeFirstStudent() {
        return queue.remove(0);
    }

    public List<Student> getQueue() {
        return new ArrayList<>(queue);
    }

}

