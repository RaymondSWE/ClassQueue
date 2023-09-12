package com.example.server.service;

import com.example.server.models.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class QueueService {

    private static final Logger logger = LoggerFactory.getLogger(QueueService.class);

    private final List<Student> queue = new ArrayList<>();

    public int manageStudent(String name, String clientId) {
        Student existingStudent = queue.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElse(null);
                logger.info(existingStudent.getName());
int ticket=-1;
        if (existingStudent == null) {
            List<String> clientIds = new ArrayList<>();
            clientIds.add(clientId);
            Student newStudent = new Student(name, clientIds);
            addStudent(newStudent);
             ticket=queue.size()-1;
             logger.info("came to where we added soemthing to queue");
        } else if(!existingStudent.getClientIds().contains(clientId)) {
            existingStudent.getClientIds().add(clientId);
            logger.info("existing");
            for(int i=0; i<queue.size(); i++)
            {
                if(queue.get(i).getName().equals(existingStudent.getName()))
                {
                ticket=i;
                break;
                }
            }
        }
        return ticket;
    }

    private void addStudent(Student student) {
        if(!queue.contains(student)) {
            queue.add(student);
            logger.info("Student added: " + student.getName());
        }
    }
    public void removeStudentByName(String name) {
        queue.removeIf(student -> student.getName().equals(name));
        logger.info("Student removed: " + name);
    }

    // Handle inactvice users dont know how though, function should be below.

    public List<Student> getQueue() {
        return new ArrayList<>(queue);
    }

}

