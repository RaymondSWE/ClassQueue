import uuid
from tkinter import messagebox
from config.server_handler import ServerHandler
from error.connection_exceptions import EmptyResponseError
from error.connection_exceptions import ServerError

class QueueLogic:
    def __init__(self, ui):
        self.ui = ui
        self.client_id = str(uuid.uuid4())  # unique client identifier
        self.server_handler = ServerHandler()
        self.send_heartbeat_flag = True

    def join_queue(self):
        name = self.ui.name_entry.get()
        if not name:
            messagebox.showerror("Error", "Name cannot be empty!")
            return
        self.server_handler.subscribe(name)

        data = {
            "enterQueue": True,
            "name": name,
            "clientId": self.client_id,

        }

        # Send request to serverHandler (Local server)
        server_response = self.server_handler.send_request(data, self.server_handler.req_socket)
        if not server_response:
            messagebox.showerror("Error", "Server response error.")
            return

        ticket = server_response.get('ticket', None)
        if ticket is not None:
            messagebox.showinfo("Info", f"Joined the queue with ticket number: {ticket}")
            self.ui.start_heartbeat()  # Heartbeats should be sent after it has joined the queue (I think)
            self.send_heartbeat_flag = True

        else:
            messagebox.showerror("Error", "Failed to join the queue.")

    def send_heartbeat(self):
        if not self.send_heartbeat_flag:
            return
        try:
            # Send heartbeat
            server_response = self.server_handler.send_request({
                "type": "heartbeat",
                "name": self.ui.name_entry.get(),
            }, self.server_handler.req_socket)
            if "error" in server_response:
                raise ServerError(server_response.get("message"))
        except EmptyResponseError:
            messagebox.showerror("Error", "Empty response received from the server.")

    def stop_heartbeat(self):
        self.send_heartbeat_flag = False

    def listen_for_updates(self):
        update = self.server_handler.check_for_updates()
        if update:
            topic, data = update
            if topic == "queue":
                self.ui.update_queue(data)
            elif topic == "supervisors":
                self.ui.update_supervisors(data)
            elif topic == self.ui.name_entry.get():
                self.display_user_message(data)

    def display_user_message(self, data):
        supervisor_name = data.get("supervisor")
        message = data.get("message")
        messagebox.showinfo("Supervisor Message", f"Message from {supervisor_name}: {message}")
        self.stop_heartbeat()
