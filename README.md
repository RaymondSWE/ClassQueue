# Distrubted system  for queue system - API documentation


## Authors
a21nibah
a21rammo
## Getting started
You need to have the following installed in order to run this project
- java 17 or later
- python3.12 or later
- a mavon environment
### Server
In order to run the server open a terminal  and navigate to  the server directory which can be found at the root of the project.
Before  you can run the server run the following command to install all the dependencies:
```console
mvn install
```
After the dependencies have been installed successfully use the following command to run the server:
```console
mvn spring-boot:run
```
### Client
The client directory is located at the root of the project. Open a terminal and navigate to "python-client" and run the following command to install the required librarys:
```console
pip install -r requirements.txt
```
The student and supervisor client is located under python-client/src and can be run using the following command:
```console
python student.py
python supervisor_client.py
```
## API
### Requests and responses
#### Connection request
Sent to the server to verify that a connection has been established
```json
{"type": "startup", "client_number": "<a unique number for the client>"}
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
{"ticket": <index, represents the users' place in the queue>, "name":"<name>"}
```
#### Heartbeats
Sent to the server in order to maintain the students' place in the queue. If the client is inactive for more than 4 seconds it's removed from the queue.
```json
{"ttype": "heartbeat", "name":"<student name>", "clientID":"<unique id string>"}
```
server response:
```
An empty JSON string
```
#### Connect as supervisor
Sent to the server in order to connect as a supervisor instead of a student
```json
{"type": "supervisor", "supervisorName": "<name of the supervisor>", "addSupervisor": true}
```
server response:
```json
{"status":"<success>", "message": "<a message indicating that the supervisor was added>"}
```
#### Attend the queue
Sent to the server when a supervisor attends a student in the queue
```json
{"message":"<the message that is going to be sent to the student when they are informed about their turn to be attended.>", "type":"supervisor", "attendStudent":true, "supervisorName": "<name of the supervisor that is going to attend the student>"}
```
server response:
```json
{"status": "success", "mesage":"<a message which indicates that a student is being attended>"}
```
#### Change the supervisor status to available
Sent to the server in order to make the supervisor available
```json
{"type": "supervisor", "makeAvailable": true, "supervisorName":"<name of the supervisor that is going to be made available>"}
```
server response:
```json
{"status": "<success>", "message":"<message which indicates that the supervisors' status has been changed>"}
```
#### Errors
The server sends the following when an error is encountered:
```json
{"message": "<a description of the error>", "error":"<error type, invalidMessage>"}
```
### Broadcasts
#### Queue status
Sent to all clients in response to any changes in the queue, for example new clients entering the queue or students receiving supervision. The queue status is an ordered array of Queue tickets, where the first element represent the first student in the queue.
```json
Topic: queue
[ 
    {"ticket": <index>, "name": "<name>"}, ... 
]
```
#### Supervisor status
Sent to all clients in response to any changes in the list of supervisors, for example new supervisors connecting or when the status of a supervisor changes.
```json
Sent by: server
Topic: supervisors
[ 
    {"name": <name>, "status": "pending"|"available"|"occupied", "client": undefined|{"ticket":<index>,"name":"<name>"}}, ... 
]
```
#### Student messages
the server sends messages specified by the supervisor to the student that is being attended.
```json
Topic: <name of user>
{
    "supervisor":"<name of supervisor>",
    "message":"<message from supervisor>"
}
```
