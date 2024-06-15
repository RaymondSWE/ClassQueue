### Requests and responses

#### Connection request
Sent to the server to verify that a connection has been established
```json
{
    "type": "startup",
    "client_number": "<a unique number for the client>"
}
```
**Server Response:**
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

**Server Response:**

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

**Server Response:**
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

**Server Response:**
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
**Server Response:**
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
**Server Response:**
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
the server sends messages specified by the supervisor to the student being attended.
```json
{
    "supervisor":"<name of supervisor>",
    "message":"<message from supervisor>"
}
```
