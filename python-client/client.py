import zmq
import tkinter as tk
from tkinter import ttk, messagebox, Listbox
import json
import uuid

context = zmq.Context()

# Subscriber socket to listen for queue updates
sub_socket = context.socket(zmq.SUB)
sub_socket.setsockopt_string(zmq.SUBSCRIBE, 'queue')
sub_socket.connect('tcp://ds.iit.his.se:5555')

# Request socket to send commands and receive responses
req_socket = context.socket(zmq.REQ)
req_socket.connect('tcp://ds.iit.his.se:5556')

class QueueClient(tk.Tk):
    def __init__(self):
        super().__init__()

        self.title("Queue Client")
        self.geometry("400x300")

        self.style = ttk.Style()
        self.style.configure("TButton", background="#00b09b")

        self.client_id = str(uuid.uuid4())  # unique client identifier
        self.create_widgets()

    def create_widgets(self):
        self.input_section = ttk.Frame(self, padding="20")
        self.input_section.grid(row=0, column=0, sticky="ew")

        self.name_entry = ttk.Entry(self.input_section)
        self.name_entry.grid(row=0, column=0, sticky="ew", padx=5)

        self.join_queue_button = ttk.Button(self.input_section, text="Join Queue", command=self.join_queue)
        self.join_queue_button.grid(row=0, column=1, padx=5)

        self.queue_section = ttk.Frame(self, padding="20")
        self.queue_section.grid(row=1, column=0, sticky="ew")

        self.queue_label = ttk.Label(self.queue_section, text="Students in Queue")
        self.queue_label.grid(row=0, column=0, columnspan=2)

        self.queue_listbox = Listbox(self.queue_section, height=10)
        self.queue_listbox.grid(row=1, column=0, columnspan=2)

        # Listen for messages and send heartbeats
        self.after(1000, self.listen_for_updates)
        self.after(3000, self.send_heartbeat)  # Send heartbeat every 3 seconds

    def join_queue(self):
        name = self.name_entry.get()
        if not name:
            messagebox.showerror("Error", "Name cannot be empty!")
            return
        print("Joining queue with name:", name)

        # Send request to join the queue
        req_socket.send_json({
            "enterQueue": True,
            "name": name,
            "clientId": self.client_id
        })
        # Get the response from the server
        response = req_socket.recv_json()
        ticket = response.get('ticket', None)
        if ticket:
            messagebox.showinfo("Info", f"Joined the queue with ticket number: {ticket}")
        else:
            messagebox.showerror("Error", "Failed to join the queue.")

    def send_heartbeat(self):
        req_socket.send_json({
            "name": self.name_entry.get(),
            "clientId": self.client_id
        })
        response = req_socket.recv_json()
        error = response.get('error', None)
        if error:
            messagebox.showerror("Server Error", response['msg'])
        self.after(3000, self.send_heartbeat)

    def listen_for_updates(self):
        try:
            # Check for new messages
            topic, msg = sub_socket.recv_multipart(zmq.NOBLOCK)
            students = json.loads(msg.decode())
            self.update_queue(students)
        except zmq.Again:
            pass
        self.after(1000, self.listen_for_updates)

    def update_queue(self, students):
        self.queue_listbox.delete(0, tk.END)
        for student in students:
            self.queue_listbox.insert(tk.END, student['name'])


if __name__ == "__main__":
    app = QueueClient()
    app.mainloop()
