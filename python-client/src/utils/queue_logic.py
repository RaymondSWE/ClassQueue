import uuid
from tkinter import messagebox
from network.zmq_handler import ZMQHandler


class QueueLogic:
    def __init__(self, ui):
        self.ui = ui
        self.client_id = str(uuid.uuid4())  # unique client identifier
        self.zmq_handler = ZMQHandler()

    def join_queue(self):
        name = self.ui.name_entry.get()
        if not name:
            messagebox.showerror("Error", "Name cannot be empty!")
            return
        print("Joining queue with name:", name)

        response = self.zmq_handler.send_request({
            "enterQueue": True,
            "name": name,
            "clientId": self.client_id
        }, self.zmq_handler.req_socket)

        ticket = response.get('ticket', None)
        if ticket:
            messagebox.showinfo("Info", f"Joined the queue with ticket number: {ticket}")
        else:
            messagebox.showerror("Error", "Failed to join the queue.")

    def send_heartbeat(self):
        response = self.zmq_handler.send_request({
            "name": self.ui.name_entry.get(),
            "clientId": self.client_id
        }, self.zmq_handler.req_socket)
        error = response.get('error', None)
        if error:
            messagebox.showerror("Server Error", response['msg'])

    def listen_for_updates(self):
        students = self.zmq_handler.check_for_updates()
        if students:
            self.ui.update_queue(students)
