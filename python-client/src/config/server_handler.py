import zmq
import json

class serverHandler:
    def __init__(self):
        self.context = zmq.Context()

        # Connect to server response socket
        self.req_socket = self.context.socket(zmq.REQ)
        self.req_socket.connect("tcp://localhost:5600")

        # This seems to be another request socket in your code. Configure it similarly.
        self.srvReqSocket = self.context.socket(zmq.REQ)
        self.srvReqSocket.connect("tcp://localhost:5600")

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