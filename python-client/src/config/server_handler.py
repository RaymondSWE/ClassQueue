import zmq
import json
from error.connection_exceptions import ConnectionError


class ServerHandler:
    def __init__(self):
        self.context = zmq.Context()
        try:
            # request socket
            self.req_socket = self.context.socket(zmq.REQ)
            self.req_socket.connect("tcp://localhost:5600")
        except zmq.ZMQError:
            raise ConnectionError("Error connecting to server")

    def send_request(self, message, socket):
        socket.send_json(message)
        response_data = socket.recv()

        # Check for Empty Responses
        if not response_data.strip():
            print("Received empty data")
            return None

        # Check if it's valid JSON
        if response_data.startswith(b'{') and response_data.endswith(b'}'):
            try:
                return json.loads(response_data)
            except json.JSONDecodeError:
                print("Error decoding JSON:", response_data)
                return None
        else:
            # Handle plain string responses here, if needed
            print("Received non-JSON response:", response_data)
            return None
