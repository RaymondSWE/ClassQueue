import zmq
import tkinter as tk
from tkinter import simpledialog, messagebox, Listbox
import uuid

# ZeroMQ setup
context = zmq.Context()
requester = context.socket(zmq.REQ)
requester.connect('tcp://ds.iit.his.se:5556')

def enter_queue():
    name = name_entry.get()
    client_id = str(uuid.uuid4())
    message = {
        "enterQueue": True,
        "name": name,
        "clientId": client_id
    }
    requester.send_string(str(message))
    response = requester.recv_string()
    messagebox.showinfo("Response", response)

    # In a real scenario, you might want to update the queue list based on the response or other mechanisms.

app = tk.Tk()
app.title("Queue Client")

name_label = tk.Label(app, text="Enter your name")
name_label.pack(pady=10)

name_entry = tk.Entry(app)
name_entry.pack(pady=10)

enter_button = tk.Button(app, text="Join Queue", command=enter_queue)
enter_button.pack(pady=10)

# This is just a placeholder. In a real scenario, you might want to fill this based on the server's response.
queue_label = tk.Label(app, text="Students in Queue")
queue_label.pack(pady=10)

queue_list = Listbox(app)
queue_list.pack(pady=10, padx=20)

app.mainloop()
