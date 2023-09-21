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
    //sends a message to the user currently being attended.
    private void broadcastMessage(String supervisorName, String message, String topic)
    {
        JSONObject json=new JSONObject();
        json.put("supervisor", supervisorName);
        json.put("message", message);
        String broadcastMessage=json.toString();
        logger.info("sending broadcast message: ", broadcastMessage);
        zmqPublisherSocket.sendMore(topic);
        zmqPublisherSocket.send(broadcastMessage);
    }


    //processes client request
    public String processSupervisorRequest(String request)
    {
        try
        {
            JSONObject json=new JSONObject(request);
            String message=json.getString("message");
            this.message=message;
            Student student=queueService.removeFirstStudent();
            JSONObject response=new JSONObject();
            response.put("name", student.getName());
            broadcastMessage(json.getString("supervisorName"), message, student.getName());
            return response.toString();
        }
        catch(JSONException e)
        {
            logger.error("error parsing supervisor request", e);
            return "bad response";
        }
    }

    //handles incoming requests from the supervisor
/* 
private void handleClientRequest()
{
    
while(keepRunning)
{
     
    String request=zmqResponseSocket.recvStr();
    if(request!=null)
    {
String response=processClientRequest(request);
//if(response.equals("bad response"))
//continue;
logger.info("request processed");
zmqResponseSocket.send(response);
    }
    
}

}
*/
    @Override
    public void run() {
        // TODO Auto-generated method stub
        //handleClientRequest();
    }
/* 
public void stop()
{
    this.keepRunning=false;
}
*/
}
