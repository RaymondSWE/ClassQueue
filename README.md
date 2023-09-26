# Distrubted system  for queue system - API documentation


## ResponderWorker

#### Description
`ResponderWorker` handles incoming client requests and sends responses back.

### Methods

- `run()`: Implements the Runnable interface run method. Inorder to handle client requests.
- `handleClientRequest()`: Handles incoming client requests and responds.
- `handleErrorMessage(String errorType, String message)`: Sends an error message through the socket.
- `handleHeartbeat(JSONObject jsonRequest)`: Handles heartbeat messages from clients.
- `handleStartupMessage(JSONObject jsonRequest)`: Handles startup messages from clients.
- `handleSupervisorRequest(JSONObject jsonRequest)`: Handles incoming supervisor requests.
- `processClientRequest(JSONObject json)`: Processes client requests and gives them a ticket


## PublisherWorker

### Description
`PublisherWorker` is responsible for broadcasting updated queue and supervisor status. It also handles student message for specific students.

### Methods

- `run()`: Implements the Runnable interface's run method.
- `broadcastQueue(List<Student> queue)`: Broadcasts the current queue of students.
- `convertQueueToJson(List<Student> queue)`: Converts the student queue to a JSON.
- `sendUserMessage(String supervisorName, String userName, String message)`: Sends a student message.
- `broadcastSupervisorsStatus()`: Broadcasts the status of all connected supervisors.

## StudentService

### Description
`StudentService` manages the student queue and performs operations such as adding, removing, and updating students in the queue. 
We tried to keep everything related to publish and responding out of the service folder, in order to keep a good code structure.

### Methods

- `getTicket()`: Returns the ticket number for students entering queue
- `manageStudent(String name, String clientId)`: Manages the addition of a student to the queue.
- `updateClientHeartbeat(String clientId)`: Updates the last heartbeat received from a client.
- `removeInactiveStudents()`: Scheduled task to remove inactive students from the queue with help of springboot annotation.
- `removeFirstStudent()`: Removes the first student in the queue.
- `getQueue()`: Returns the current student queue.

## SupervisorService

### Description
`SupervisorService` manages the supervisors, handling their status and attended to which student. It is responsible for adding new supervisors, displaying connected supervisors, and managing the attendance of students by supervisors.

### Methods

- `addSupervisor(String supervisorName)`: Adds a new supervisor and sets their status to AVAILABLE. Publishes a `NewSupervisorEvent` and logs the connection of the supervisor.
- `displayAllConnectedSupervisors()`: Returns a list containing information about all currently connected supervisors.
- `attendStudent(String supervisorName, String message)`: Assigns a supervisor to attend the first student in the queue if available. removes student and updates supervisor statuses, broadcasts updates, and returns the attended student’s name. Logs information if no students are in the queue or if the supervisor is not available or not found.
- `makeSupervisorAvailable(String supervisorName)`: Makes a supervisor available if found and broadcasts supervisor and queue statuses. Logs an error if the supervisor is not found.




#### By Raman Mohammed & Nibar Ahmed
