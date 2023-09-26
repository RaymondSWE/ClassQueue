import tkinter as tk
from tkinter import ttk, Listbox, messagebox
from ttkthemes import ThemedTk

from utils.queue_logic import QueueLogic


class QueueUI(ThemedTk):
    def __init__(self):
        super().__init__()

        self.set_theme("adapta")
        self.title("Student Client")
        self.geometry("900x600")

        # frame
        content_frame = ttk.Frame(self)
        content_frame.pack(fill=tk.BOTH, expand=True)

        # outer frame
        outer_frame = ttk.Frame(content_frame)
        outer_frame.grid(row=0, column=0, sticky="nsew", padx=20, pady=20)

        # Host and Port Input section
        host_port_section = ttk.Frame(outer_frame)
        host_port_section.grid(row=2, column=0, columnspan=2, sticky="ew", pady=20)

        ttk.Label(host_port_section, text="Host:", font=("Poppins", 12)).grid(row=0, column=0, padx=10, pady=10)
        self.host_entry = ttk.Entry(host_port_section, font=("Poppins", 12), width=20)
        self.host_entry.grid(row=0, column=1, padx=10, pady=10)

        ttk.Label(host_port_section, text="SUB Port:", font=("Poppins", 12)).grid(row=0, column=2, padx=10, pady=10)
        self.sub_port_entry = ttk.Entry(host_port_section, font=("Poppins", 12), width=10)
        self.sub_port_entry.grid(row=0, column=3, padx=10, pady=10)

        ttk.Label(host_port_section, text="REQ Port:", font=("Poppins", 12)).grid(row=0, column=4, padx=10, pady=10)
        self.req_port_entry = ttk.Entry(host_port_section, font=("Poppins", 12), width=10)
        self.req_port_entry.grid(row=0, column=5, padx=10, pady=10)

        # Status Label for showing connection information
        self.status_label = ttk.Label(content_frame, text="", font=("Poppins", 12), anchor="e")
        self.status_label.grid(row=1, column=0, sticky="ew", padx=10, pady=10)

        # set default values
        self.host_entry.insert(0, "localhost")
        self.sub_port_entry.insert(0, "5500")
        self.req_port_entry.insert(0, "5600")

        # Input section
        input_section = ttk.Frame(outer_frame)
        input_section.grid(row=0, column=0, columnspan=2, sticky="ew", pady=20)
        ttk.Label(input_section, text="Name:", font=("Poppins", 12)).grid(row=0, column=0, padx=10, pady=10)
        self.name_entry = ttk.Entry(input_section, font=("Poppins", 12), width=30)
        self.name_entry.grid(row=0, column=1, padx=10, pady=10)

        self.connect_button = ttk.Button(host_port_section, text="Connect", command=self.connect_to_server)
        self.connect_button.grid(row=0, column=6, padx=10, pady=10)

        self.logic = QueueLogic(self)

        self.join_queue_button = ttk.Button(input_section, text="Join Queue", state=tk.DISABLED)
        self.join_queue_button.grid(row=0, column=2, padx=10, pady=10)




        # Queue section
        queue_section = ttk.Frame(outer_frame)
        queue_section.grid(row=1, column=0, sticky="nsew", pady=20)
        ttk.Label(queue_section, text="Students in Queue", font=("Poppins", 16, "bold")).grid(row=0, column=0, pady=10)
        self.queue_listbox = Listbox(queue_section, height=10, bg="#f5f5f5", fg="black",
                                     selectbackground="#00b09b", selectforeground="white", borderwidth=1,
                                     highlightthickness=0, font=("Poppins", 12))
        self.queue_listbox.grid(row=1, column=0, pady=10, padx=10)

        # Supervisor section
        supervisor_section = ttk.Frame(outer_frame)
        supervisor_section.grid(row=1, column=1, sticky="nsew", pady=20)
        ttk.Label(supervisor_section, text="Available Supervisors", font=("Poppins", 14, "bold")).grid(row=0, column=0,
                                                                                                       pady=10)
        self.supervisor_listbox = Listbox(supervisor_section, height=10, width=35, bg="#f5f5f5", fg="black",
                                          selectbackground="#00b09b", selectforeground="white", borderwidth=1,
                                          highlightthickness=0, font=("Poppins", 12))
        self.supervisor_listbox.grid(row=1, column=0, pady=10, padx=10)

        outer_frame.columnconfigure(0, weight=1)
        outer_frame.rowconfigure(0, weight=1)
        outer_frame.rowconfigure(1, weight=3)



    def update_queue(self, queue_data):
        self.queue_listbox.delete(0, tk.END)
        for student in queue_data:
            self.queue_listbox.insert(tk.END, student['name'])

    def start_heartbeat(self):
        self.send_heartbeat()

    def send_heartbeat(self):
        self.logic.send_heartbeat()
        self.after(3000, self.send_heartbeat)

    def update_supervisors(self, supervisors_data):
        self.supervisor_listbox.delete(0, tk.END)
        for supervisor in supervisors_data:
            name = supervisor.get('name', '')
            status = supervisor.get('status', '')
            client = supervisor.get('client', '')
            self.supervisor_listbox.insert(tk.END, f"{name} - {status} - {client}")

    def connect_to_server(self):
        self.logic.connect_to_server()


if __name__ == "__main__":
    app = QueueUI()
    app.mainloop()
