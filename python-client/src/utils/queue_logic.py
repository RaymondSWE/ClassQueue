import uuid
from tkinter import messagebox
from config.zmq_handler import ZMQHandler
from config.server_handler import ServerHandler


class QueueLogic:
    def __init__(self, ui):
        self.ui = ui
        self.client_id = str(uuid.uuid4())  # unique client identifier
        self.zmq_handler = ZMQHandler()
        self.server_handler = ServerHandler()

    def join_queue(self):
        name = self.ui.name_entry.get()
        if not name:
            messagebox.showerror("Error", "Name cannot be empty!")
            return

        # Send request to ZMQHandler (API)
        api_response = self.zmq_handler.send_request({
            "enterQueue": True,
            "name": name,
            "clientId": self.client_id
        }, self.zmq_handler.req_socket)

        # Send request to serverHandler (Local server)
        server_response = self.server_handler.send_request({
            "enterQueue": True,
            "name": name,
            "clientId": self.client_id
        }, self.server_handler.req_socket)

        # Handle None response
        if not api_response:
            messagebox.showerror("Error", "API response error.")
            return

        if not server_response:
            messagebox.showerror("Error", "Server response error.")
            return

        ticket = api_response.get('ticket', None)
        if ticket:
            messagebox.showinfo("Info", f"Joined the queue with ticket number: {ticket}")
        else:
            messagebox.showerror("Error", "Failed to join the queue.")



    def send_heartbeat(self):
        # Send heartbeat to ZMQHandler (API)
        api_response = self.zmq_handler.send_request({
            "name": self.ui.name_entry.get(),
            "clientId": self.client_id
        }, self.zmq_handler.req_socket)

        # Send heartbeat to serverHandler (Local server)
        server_response = self.server_handler.send_request({
            "name": self.ui.name_entry.get(),
            "clientId": self.client_id
        }, self.server_handler.req_socket)

        # Check error in API response
        error = api_response.get('error', None)
        if error:
            messagebox.showerror("Server Error", api_response['msg'])

    def listen_for_updates(self):
        students = self.zmq_handler.check_for_updates()
        if students:
            self.ui.update_queue(students)
