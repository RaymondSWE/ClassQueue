package com.example.server.service;

import com.example.server.models.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class QueueService {

    private static final Logger logger = LoggerFactory.getLogger(QueueService.class);

    // A queue to maintain FIFO order of students
    private final LinkedList<Student> queue = new LinkedList<>();

    public void addStudent(Student student) {
        if(!queue.contains(student)) {
            queue.add(student);
            logger.info("Student added: " + student.getName());
        }
    }
    public void removeStudentByName(String name) {
        queue.removeIf(student -> student.getName().equals(name));
        logger.info("Student removed: " + name);
    }

    public LinkedList<Student> getQueue() {
        return queue;
    }
}
