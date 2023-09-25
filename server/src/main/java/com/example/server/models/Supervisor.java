package com.example.server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supervisor {
    private String name;
    private SupervisorStatus supervisorStatus;
    private Student attendingStudent;
    private String messageFromSupervisor;
}