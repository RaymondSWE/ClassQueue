# Distributed queue system - API documentation


## Authors
 * **Nibar Ahmed** (a21nibar)
 * **Raman Mohammed** (a21rammo)

## Introduction
This project implements a distributed queue system with a client-server architecture. The server is built using Java and Spring Boot, while the client is developed in Python with a Tkinter-based GUI.

## Reflection
During the development, we maintained a structured workflow with protected main branches, requiring pull requests and code reviews. Manual tests were conducted on each branch before merging PRs. The project was both enjoyable and educational.

## Getting started

You need to have the following installed in order to run this project:

- Java 17 or later
- Python 3.12 or later
- A Maven environment
  
### Setting Up the server
1. Open a terminal and navigate to the `server` directory, located at the root of the project.
2. Run the following command to install all the dependencies:
```console
mvn install
```
After the dependencies have been installed successfully, use the following command to run the server:
```console
mvn spring-boot:run
```
### Setting Up the client

1. The client directory is located at the root of the project. Open a terminal and navigate to python-client.
2. Run the following command to install the required libraries:
   
```console
pip install -r requirements.txt
```

3. The student and supervisor client is located under python-client/src and can be run using the following command:
   
```console
python student.py
python supervisor_client.py
```

## API Documentation

### Requests and responses

#### Connection request
Sent to the server to verify that a connection has been established
```json
{
    "type": "startup",
    "client_number": "<a unique number for the client>"
}
```
server response:
```

"Acknowledged startup"

```
#### Enter the queue
Indicates that a user with specified name wants to enter the queue.
A single user may connect through several clients. If another client with the same name is already connected, both clients hold the same place in the queue.
```json
{
    "enterQueue": true,
    "name": "<name>",
    "clientId": "<unique id string>"
}
```

server response:

```json
{
    "ticket": <index represents the users' place in the queue>
     "name":"<name>"
}
```

#### Heartbeats
Sent to the server in order to maintain the students' place in the queue. If the client is inactive for more than 4 seconds it's removed from the queue.

```json
{
    "type": "heartbeat"
    "name":"<student name>"
    "clientID":"<unique id string>"
}
```

server response:
```
An empty JSON string
```

#### Connect as supervisor
Sent to the server in order to connect as a supervisor instead of a student
```json
{
    "type": "supervisor"
     "supervisorName": "<name of the supervisor>"
     "addSupervisor": true
}
```

server response:
```json
{
    "status":"<success>"
    "message": "<a message indicating that the supervisor was added>"
}
```

#### Attend the queue
Sent to the server when a supervisor attends a student in the queue
```json
{
    "message":"<the message that is going to be sent to the student when they are informed about their turn to be attended.>"
     "type":"supervisor", "attendStudent":true
    "supervisorName": "<name of the supervisor that is going to attend the student>"
}
```
server response:
```json
{
    "status": "success"
     "mesage":"<a message which indicates that a student is being attended>"
}
```
#### Change the supervisor status to available
Sent to the server in order to make the supervisor available
```json
{
"type": "supervisor"
     "makeAvailable": true
     "supervisorName":"<name of the supervisor that is going to be made available>"
}
```
server response:
```json
{
    "status": "<success>",
    "message":"<message which indicates that the supervisors' status has been changed>"
}
```
#### Errors
The server sends the following when an error is encountered:
```json
{
    "message": "<a description of the error>"
     "error":"<error type, invalidMessage>"
}
```
### Broadcasts
#### Queue status
Sent to all clients in response to any changes in the queue, for example new clients entering the queue or students receiving supervision. The queue status is an ordered array of Queue tickets, where the first element represent the first student in the queue.
```json

{
    "ticket": <index>
     "name": "<name>"
}, 

```
#### Supervisor status
Sent to all clients in response to any changes in the list of supervisors, for example new supervisors connecting or when the status of a supervisor changes.

```json
{
    "name": <name>
     "status": "pending"|"available"|"occupied",
    "client": undefined|{"ticket":<index>"
    "studentName":"<name>"
}}
```

#### Student messages
the server sends messages specified by the supervisor to the student that is being attended.
```json
{
    "supervisor":"<name of supervisor>",
    "message":"<message from supervisor>"
}
```




#### AsyncConfig
- Configuration to enable asynchronous execution. The method will only require @async annotation to be async.


