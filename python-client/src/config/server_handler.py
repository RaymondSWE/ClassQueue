import json
import zmq
from error.connection_exceptions import (ConnectionError, EmptyResponseError,
                                         DeserializationError, SendMessageError, InvalidResponseError)


class ServerHandler:
    client_number = 1

    def __init__(self):
        self.context = zmq.Context()
        try:
            # request socket
            self.req_socket = self.context.socket(zmq.REQ)
            self.req_socket.connect("tcp://localhost:5600")

            # subscriber socket
            self.sub_socket = self.context.socket(zmq.SUB)
            self.sub_socket.connect("tcp://localhost:5500")
            self.sub_socket.setsockopt_string(zmq.SUBSCRIBE, "queue")
        except zmq.ZMQError:
            raise ConnectionError("Error connecting to server")

    def check_for_updates(self):
        try:
            # Check for new messages
            msg = self.sub_socket.recv_string(zmq.NOBLOCK)

            # Check if the message is empty or not valid JSON
            if not msg or not (msg.startswith('{') or msg.startswith('[')):
                return None

            return json.loads(msg)
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
            if(response_data=="bad response"):
                return "bad response"
            else:
                raise InvalidResponseError("Received non-JSON response from server.")
        except zmq.ZMQError:
            raise SendMessageError("Error sending message to server.")
        except json.JSONDecodeError:
            raise DeserializationError("Error decoding JSON from server.")

    def send_startup_message(self):
        try:
            self.req_socket.send_json({"type": "startup", "client_number": ServerHandler.client_number})
            ServerHandler.client_number += 1
            reply = self.req_socket.recv()
            print("startup message:", reply)
        except zmq.ZMQError:
            print("Error sending startup message to server.")
