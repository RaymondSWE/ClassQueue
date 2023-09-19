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
private String name;
private int ticket=-1;


public int getTicket() {
                for(int i=0; i<queue.size(); i++)
            {
                if(queue.get(i).getName().equals(this.name))
                {
                this.ticket=i;
                break;
                }
            }

    return ticket;
}

    public void manageStudent(String name, String clientId) {
        this.name=name;
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
        } else if(!existingStudent.getClientIds().contains(clientId)) {
            existingStudent.getClientIds().add(clientId);
        }
    }

    private void addStudent(Student student) {
        if(!queue.contains(student)) {
            queue.add(student);
        }
    }
    public void removeStudentByName(String name) {
        queue.removeIf(student -> student.getName().equals(name));
        logger.info("Student removed: " + name);
    }
//remove the first student in queue
public Student removeFirstStudent()
{
    return queue.remove(0);
}
    // Handle inactvice users dont know how though, function should be below.

    public List<Student> getQueue() {
        return new ArrayList<>(queue);
    }

}

