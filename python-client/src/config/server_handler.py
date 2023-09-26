import json
import logging
import time
import zmq
from error.connection_exceptions import (
    ConnectionError, DeserializationError, EmptyResponseError,
    InvalidResponseError, SendMessageError
)


class ServerHandler:
    CLIENT_NUMBER = 1
    MAX_RETRIES = 3
    RETRY_INTERVAL = 5

    def __init__(self, host, sub_port, req_port):
        self.REQ_SOCKET_ADDRESS = f"tcp://{host}:{req_port}"
        self.SUB_SOCKET_ADDRESS = f"tcp://{host}:{sub_port}"
        self.context = zmq.Context()

    def connect(self):
        retries = 0
        while retries < self.MAX_RETRIES:
            try:
                self.req_socket = self.context.socket(zmq.REQ)
                self.req_socket.setsockopt(zmq.RCVTIMEO, 5000)  # Set a timeout for receive operations
                self.req_socket.connect(self.REQ_SOCKET_ADDRESS)

                self.sub_socket = self.context.socket(zmq.SUB)
                self.sub_socket.connect(self.SUB_SOCKET_ADDRESS)
                self.sub_socket.setsockopt_string(zmq.SUBSCRIBE, "queue")
                self.sub_socket.setsockopt_string(zmq.SUBSCRIBE, "supervisors")

                # Testing connection by sending a ping or a simple message.
                self.req_socket.send_string("queue")
                # If server is offline, it will throw a zmq.Again exception.
                self.req_socket.recv_string()
                return True  # Connection successful

            except zmq.Again:
                logging.warning("Timeout while waiting for a response from server.")
            except zmq.ZMQError as e:
                logging.error(f"ZMQ Error: {e}")
            except Exception as e:
                logging.error(f"Unexpected Error: {e}")
            finally:
                retries += 1
                logging.warning(
                    f"Error connecting to server. Retrying {retries}/{self.MAX_RETRIES} in {self.RETRY_INTERVAL} seconds...")
                time.sleep(self.RETRY_INTERVAL)

        logging.error("Connection failed after reaching the maximum number of retries.")
        return False  # Connection failed

    def subscribe(self, topic):
        self.sub_socket.setsockopt_string(zmq.SUBSCRIBE, topic)

    def check_for_updates(self):
        try:
            topic = self.sub_socket.recv_string(flags=zmq.NOBLOCK)
            msg = self.sub_socket.recv_string(flags=zmq.NOBLOCK)
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
            if not response_data.strip():
                raise EmptyResponseError("Received empty data from server.")
            if response_data.startswith(b'{') and response_data.endswith(b'}'):
                return json.loads(response_data)
            raise InvalidResponseError("Received non-JSON response from server.")
        except zmq.ZMQError:
            raise SendMessageError("Error sending message to server.")
        except json.JSONDecodeError:
            raise DeserializationError("Error decoding JSON from server.")

    def send_startup_message(self):
        try:
            self.req_socket.send_json({"type": "startup", "client_number": self.CLIENT_NUMBER})
            self.CLIENT_NUMBER += 1
            reply = self.req_socket.recv()
            print("Startup message:", reply)
        except zmq.ZMQError:
            print("Error sending startup message to server.")