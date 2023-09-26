##authors
a21nibah
a21rammo
## getting started
you need to have the following installed in order to run this project
- java 17 or later
- python3.12 or later
- a mavon environment
###server
in order to run the server open a terminal  and navigate to  the server directory which can be found at the root of the project.
before  you can run the server run the following command to install all the dependencies:
`mvn install`
after the dependencies have been installed successfully use the following command to run the server:
`mvn spring-boot:run`
###client
the client directory is located at the root of the project. Open a terminal and navigate to `python-client` and run the following command to install the required librarys:
`pip install -r requirements.txt`
the student and supervisor client is located under python-client/src and can be run using the following command:
`python client.py`
`python supervisor-client.py`
##API
###requests
####connection request
sent to the server to verify that a connection has been established
{"type": "startup", "client_number": "<a unique number for the client>"}
server response:
"Acknowledged startup"

####enter the queue
Indicates that a user with specified name wants to enter the queue.
A single user may connect through several clients. If another client with the same name is already connected, both clients hold the same place in the queue.
{
    "enterQueue": true,
    "name": "<name>",
    "clientId": "<unique id string>"
}
server response:
{"ticket": <index, represents the users' place in the queue>, "name":"<name>"}
####heartbeats
sent to the server in order to maintain the students' place in the queue. If the client is inactive for more than 4 seconds it's removed from the queue.
{"ttype": "heartbeat", "name":"<student name>", "clientID":"<unique id string>"}
server response:
an empty JSON string
####connect as supervisor
sent to the server in order to connect as a supervisor instead of a student
{"type": "supervisor", "supervisorName": "<name of the supervisor>", "addSupervisor": true}
server response:
{"status":"<success>", "message": "<a message indicating that the supervisor was added>"}
####attend the queue
sent to the server when a supervisor attends a student in the queue
{"message":"<the message that is going to be sent to the student when they are informed about their turn to be attended.>", "type":"supervisor", "attendStudent":true, "supervisorName": "<name of the supervisor that is going to attend the student>"}
server response:
{"status": "success", "mesage":"<a message which indicates that a student is being attended>"}
#### change the supervisor status to available
sent to the server in order to make the supervisor available
{"type": "supervisor", "makeAvailable": true, "supervisorName":"<name of the supervisor that is going to be made available>"}
server response:
{"status": "<success>", "message":"<message which indicates that the supervisors' status has been changed>"}
####errors
the server sends the following when an error is encountered:
{"message": "<a description of the error>", "error":"<error type, invalidMessage>"}
###broadcasts
####queue status
Sent to all clients in response to any changes in the queue, for example new clients entering the queue or students receiving supervision. The queue status is an ordered array of Queue tickets, where the first element represent the first student in the queue.
Topic: queue
[ 
    {"ticket": <index>, "name": "<name>"}, ... 
]
####supervisor status
Sent to all clients in response to any changes in the list of supervisors, for example new supervisors connecting or when the status of a supervisor changes.
Sent by: server
Topic: supervisors
[ 
    {"name": <name>, "status": "pending"|"available"|"occupied", "client": undefined|{"ticket":<index>,"name":"<name>"}}, ... 
]
####student messages
the server sends messages specified by the supervisor to the student that is being attended.
Topic: <name of user>
{
    "supervisor":"<name of supervisor>",
    "message":"<message from supervisor>"
}