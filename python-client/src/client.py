import tkinter as tk
from tkinter import ttk, simpledialog, messagebox, Listbox
import uuid
from network.zmq_handler import ZMQHandler


class QueueClient(tk.Tk):
    def __init__(self):
        super().__init__()

        self.title("Queue Client")
        self.geometry("400x300")

        self.style = ttk.Style()
        self.style.configure("TButton", background="#00b09b")

        self.client_id = str(uuid.uuid4())  # unique client identifier

        # Initialize the ZMQHandler here
        self.zmq_handler = ZMQHandler()

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

        # Listen for messages
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

    def update_queue(self, students):
        self.queue_listbox.delete(0, tk.END)
        for student in students:
            self.queue_listbox.insert(tk.END, student['name'])


if __name__ == "__main__":
    app = QueueClient()
    app.mainloop()
