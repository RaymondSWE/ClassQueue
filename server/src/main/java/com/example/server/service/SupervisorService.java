package com.example.server.service;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import com.example.server.models.Student;
import com.example.server.models.Supervisor;

@Service
public class SupervisorService implements Runnable{
    private static final org.slf4j.Logger logger=LoggerFactory.getLogger(SupervisorService.class);
@Autowired
private QueueService queueService;
@Autowired
private Socket zmqPublisherSocket;
@Autowired
private Socket zmqResponseSocket;
//we need to keep track of all the supervisors that are currently connected.
private List<Supervisor> supervisors=new ArrayList<>();
private volatile boolean keepRunning=true;
private String message;
//processes client request
/* 
private String processClientRequest(String request)
{
try
{
JSONObject json=new JSONObject(request);
String message=json.getString("message");
this.message=message;
Student student=queueService.removeFirstStudent();
JSONObject response=new JSONObject();
response.put("name", student.getName());
return response.toString();

}
catch(JSONException e)
{
logger.error("error parsing supervisor request", e);    
return "bad response";
}
}
*/
//handles incoming requests from the supervisor

private void handleClientRequest()
{
    /* 
while(keepRunning)
{
     
    String request=zmqResponseSocket.recvStr();
    if(request!=null)
    {
String response=processClientRequest(request);
logger.info("iii", response);
zmqResponseSocket.send(response);
    }
    
}
*/
}

@Override
public void run() {
    // TODO Auto-generated method stub
    handleClientRequest();
}
public void stop()
{
    this.keepRunning=false;
}

}
