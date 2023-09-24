import json

import zmq

from error.connection_exceptions import (ConnectionError, EmptyResponseError,
                                         DeserializationError, SendMessageError, InvalidResponseError)
import time
import logging
class ServerHandler:
    client_number = 1

    def __init__(self):
        self.timeout=4000 #in ms
        self.servers=0
        self.sequence=0
        self.context = zmq.Context()
        try:
            # request socket
            self.req_socket = self.context.socket(zmq.DEALER)
            #self.req_socket.connect("tcp://localhost:5600")

            # subscriber socket
            self.sub_socket = self.context.socket(zmq.SUB)
            self.sub_socket.connect("tcp://localhost:5500")
            self.sub_socket.setsockopt_string(zmq.SUBSCRIBE, "queue")
            self.sub_socket.setsockopt_string(zmq.SUBSCRIBE, "supervisors")

        except zmq.ZMQError:
            raise ConnectionError("Error connecting to server")
        # subscribe to aditional topics

    def subscribe(self, topic):
        self.sub_socket.setsockopt_string(zmq.SUBSCRIBE, topic)

    def check_for_updates(self):
        try:
            # Check for new messages
            topic = self.sub_socket.recv_string(flags=zmq.NOBLOCK)
            msg = self.sub_socket.recv_string(flags=zmq.NOBLOCK)

            # Check if the message is empty or not valid JSON
            if not msg or not (msg.startswith('{') or msg.startswith('[')):
                return None

            return (topic, json.loads(msg))
        except zmq.Again:
            return None
        except json.JSONDecodeError:
            raise DeserializationError("Error decoding JSON from server.")

    def send_request(self, message, socket):
        try:
            socket.send_json(message)
            response_data = socket.recv()

            # Check for Empty Responses
            if not response_data.strip():
                raise EmptyResponseError("Received empty data from server.")

            # Check if it's valid JSON
            if response_data.startswith(b'{') and response_data.endswith(b'}'):
                return json.loads(response_data)
            else:
                raise InvalidResponseError("Received non-JSON response from server.")
        except zmq.ZMQError:
            raise SendMessageError("Error sending message to server.")
        except json.JSONDecodeError:
            raise DeserializationError("Error decoding JSON from server.")

    def send_startup_message(self):
        try:
            data={"type": "startup", "client_number": ServerHandler.client_number, "sequence":self.sequence}
            print(data)
            self.req_socket.send_json(data)
        except zmq.ZMQError:
            print("Error sending startup message to server.")
    def connect(self, endpoint):
        self.req_socket.connect(endpoint)
        self.servers+=1
        self.sequence+=1
        for i in range(self.servers):
            self.send_startup_message()
        poll=zmq.Poller()
        poll.register(self.req_socket, zmq.POLLIN)
        response=None
        endtime=time.time()+self.timeout/1000
        while time.time()<endtime:
            sockets=dict(poll.poll((endtime-time.time())*1000))
            if(sockets.get(self.req_socket)==zmq.POLLIN):
                response=self.req_socket.recv_json()
                jsonResponse=json.loads(response)
                if jsonResponse.get("sequence")==self.sequence:
                    return jsonResponse