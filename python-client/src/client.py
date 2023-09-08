import uuid
from network.zmq_handler import ZMQHandler
from app.queue_ui import QueueUI
from tkinter import messagebox


class QueueClient(QueueUI):
    def __init__(self):
        super().__init__()

        self.client_id = str(uuid.uuid4())  # unique client identifier

        # Initialize the ZMQHandler here
        self.zmq_handler = ZMQHandler()

        # Set the command for the button
        self.join_queue_button.config(command=self.join_queue)

        # Start listening for messages
        self.after(1000, self.listen_for_updates)
        self.after(3000, self.send_heartbeat)

    def join_queue(self):
        name = self.name_entry.get()
        if not name:
            messagebox.showerror("Error", "Name cannot be empty!")
            return
        print("Joining queue with name:", name)

        response = self.zmq_handler.send_request({
            "enterQueue": True,
            "name": name,
            "clientId": self.client_id
        })

        ticket = response.get('ticket', None)
        if ticket:
            messagebox.showinfo("Info", f"Joined the queue with ticket number: {ticket}")
        else:
            messagebox.showerror("Error", "Failed to join the queue.")

    def send_heartbeat(self):
        response = self.zmq_handler.send_request({
            "name": self.name_entry.get(),
            "clientId": self.client_id
        })
        error = response.get('error', None)
        if error:
            messagebox.showerror("Server Error", response['msg'])
        self.after(3000, self.send_heartbeat)

    def listen_for_updates(self):
        students = self.zmq_handler.check_for_updates()
        if students:
            self.update_queue(students)
        self.after(1000, self.listen_for_updates)


if __name__ == "__main__":
    app = QueueClient()
    app.mainloop()
