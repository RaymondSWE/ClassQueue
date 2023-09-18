import json

import zmq

from error.connection_exceptions import EmptyResponseError, DeserializationError,  ConnectionError, InvalidResponseError, SendMessageError


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
            raise ConnectionError("Error: No connection to TinyQueue API (╯°□°)╯︵ ┻━┻")

    def send_request(self, data, socket):
        try:
            self.req_socket.send_json(data)
            response = self.req_socket.recv_json()

            # Check if the response is empty and if the request was a heartbeat
            if not response and "clientId" in data:
                return {}
            if not response:
                raise EmptyResponseError()
            return response
        except zmq.ZMQError:
            raise SendMessageError("Error: sending message to TinyQueue API (╯°□°)╯︵ ┻━┻")
        except json.JSONDecodeError:
            raise DeserializationError("Error: deserializing the response from TinyQueue API (╯°□°)╯︵ ┻━┻")

    def check_for_updates(self):
        try:
            # Check for new messages
            topic, msg = self.sub_socket.recv_multipart(zmq.NOBLOCK)
            return json.loads(msg.decode())
        except zmq.Again:
            return None