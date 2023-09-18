import json

import zmq
from error.connection_exceptions import ConnectionError, SendMessageError, DeserializationError


class ZMQHandler:

    def __init__(self):
        self.context = zmq.Context()
        try:
            # Subscriber socket to listen for queue updates
            self.sub_socket = self.context.socket(zmq.SUB)
            self.sub_socket.setsockopt_string(zmq.SUBSCRIBE, 'queue')
            self.sub_socket.connect('tcp://ds.iit.his.se:5555')

            # Request socket to send commands and receive responses
            self.req_socket = self.context.socket(zmq.REQ)
            self.req_socket.connect('tcp://ds.iit.his.se:5556')
        except zmq.ZMQError:
            raise ConnectionError("Error connecting to TinyQueue API")

    def send_request(self, data, socket):
        try:
            self.req_socket.send_json(data)
        except zmq.ZMQError:
            raise SendMessageError("Error sending data to TinyQueue API")

        try:
            # The NOBLOCK, if the api is down it will not make the client wait indefinitely. Docs:
            # https://pyzmq.readthedocs.io/en/latest/api/zmq.html
            reply = self.req_socket.recv_json(flags=zmq.NOBLOCK)
        except zmq.Again:
            return {"error": "Error no response from TinyQueue API."}
        except json.JSONDecodeError:
            raise DeserializationError("Error decoding JSON from TinyQueue API")

        return reply

    def check_for_updates(self):
        try:
            topic, msg = self.sub_socket.recv_multipart(zmq.NOBLOCK)
            return json.loads(msg.decode())
        except zmq.Again:
            return None
        except json.JSONDecodeError:
            raise DeserializationError("Error no updates received")
