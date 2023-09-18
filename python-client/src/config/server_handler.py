import json
import zmq
from error.connection_exceptions import (ConnectionError, EmptyResponseError,
                                         DeserializationError, SendMessageError, InvalidResponseError)


class ServerHandler:
    def __init__(self):
        self.context = zmq.Context()
        try:
            # request socket
            self.req_socket = self.context.socket(zmq.REQ)
            self.req_socket.connect("tcp://localhost:5600")

            # reply socket
            self.rep_socket = self.context.socket(zmq.REP)
            self.rep_socket.connect("tcp://localhost:5500")
        except zmq.ZMQError:
            raise ConnectionError("Error connecting to server")

    def check_for_updates(self):
        try:
            # Check for new messages
            topic, msg = self.rep_socket.recv_multipart(zmq.NOBLOCK)
            return json.loads(msg.decode())
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
