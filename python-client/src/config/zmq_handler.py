import json

import zmq


class ZMQHandler:
    def __init__(self):
        self.context = zmq.Context()

        # Subscriber socket to listen for queue updates
        self.sub_socket = self.context.socket(zmq.SUB)
        self.sub_socket.setsockopt_string(zmq.SUBSCRIBE, 'queue')
        self.sub_socket.connect('tcp://ds.iit.his.se:5555')

        # Request socket to send commands and receive responses
        self.req_socket = self.context.socket(zmq.REQ)
        self.req_socket.connect('tcp://ds.iit.his.se:5556')
        self.srvReqSocket=self.context.socket(zmq.REQ)
        self.srvSubSocket=self.context.socket(zmq.SUB)
        self.srvSubSocket.setsockopt_string(zmq.SUBSCRIBE, 'queue')
        self.srvReqSocket.connect("tcp://localhost:5600")
        self.srvSubSocket.connect("tcp://localhost:5500")

    def send_request(self, data, socket):
        self.req_socket.send_json(data)
        return self.req_socket.recv_json()

    def check_for_updates(self):
        try:
            # Check for new messages
            topic, msg = self.sub_socket.recv_multipart(zmq.NOBLOCK)
            return json.loads(msg.decode())
        except zmq.Again:
            return None
