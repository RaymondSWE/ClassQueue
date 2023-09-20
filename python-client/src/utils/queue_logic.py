import uuid
from tkinter import messagebox
from config.server_handler import ServerHandler
from error.connection_exceptions import EmptyResponseError


class QueueLogic:
    def __init__(self, ui):
        self.ui = ui
        self.client_id = str(uuid.uuid4())  # unique client identifier
        self.server_handler = ServerHandler()

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
        # Handle None response
        if not server_response:
            messagebox.showerror("Error", "API response error.")
            return

        if not server_response:
            messagebox.showerror("Error", "Server response error.")
            return

        ticket = server_response.get('ticket', None)
        if ticket:
            messagebox.showinfo("Info", f"Joined the queue with ticket number: {ticket}")
            self.ui.start_heartbeat()  # Heartbeats should be sent after it has joined the queue (I think)
        else:
            messagebox.showerror("Error", "Failed to join the queue.")

    def send_heartbeat(self):
        try:

            # Send heartbeat to serverHandler (Local server)
            server_response = self.server_handler.send_request({
                "name": self.ui.name_entry.get(),
                "clientId": self.client_id
            }, self.server_handler.req_socket)

        except EmptyResponseError:
            messagebox.showerror("Error", "Empty response received from the server.")

    def listen_for_updates(self):
        queue_data = self.server_handler.check_for_updates()
        if queue_data and "supervisorName" not in queue_data:
            self.ui.update_queue(queue_data)