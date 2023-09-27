import tkinter as tk
from tkinter import ttk, Listbox
from ttkthemes import ThemedTk
from utils.student_logic import QueueLogic


class QueueUI(ThemedTk):
    def __init__(self):
        super().__init__()

        self.set_theme("adapta")
        self.title("Student Client")
        self.geometry("900x600")

        # Main frame
        content_frame = ttk.Frame(self)
        content_frame.pack(fill=tk.BOTH, expand=True)

        # Input section
        input_section = ttk.Frame(content_frame)
        input_section.grid(row=0, column=0, columnspan=2, sticky="ew", pady=20)

        ttk.Label(input_section, text="Name:", font=("Poppins", 14)).grid(row=0, column=0, padx=10, pady=10)
        self.name_entry = ttk.Entry(input_section, font=("Poppins", 12), width=30)
        self.name_entry.grid(row=0, column=1, padx=10, pady=10)

        self.connect_button = ttk.Button(input_section, text="Connect", command=self.connect_to_server)
        self.connect_button.grid(row=0, column=2, padx=10, pady=10)

        self.join_queue_button = ttk.Button(input_section, text="Join Queue", state=tk.DISABLED)
        self.join_queue_button.grid(row=0, column=3, padx=10, pady=10)

        # Host and Port Input section
        host_port_section = ttk.Frame(content_frame)
        host_port_section.grid(row=1, column=0, columnspan=2, sticky="ew", pady=20)

        ttk.Label(host_port_section, text="Host:", font=("Poppins", 14)).grid(row=0, column=0, padx=10, pady=10)
        self.host_entry = ttk.Entry(host_port_section, font=("Poppins", 12), width=20)
        self.host_entry.grid(row=0, column=1, padx=10, pady=10)
        self.host_entry.insert(0, "localhost")

        ttk.Label(host_port_section, text="SUB Port:", font=("Poppins", 14)).grid(row=0, column=2, padx=10, pady=10)
        self.sub_port_entry = ttk.Entry(host_port_section, font=("Poppins", 12), width=10)
        self.sub_port_entry.grid(row=0, column=3, padx=10, pady=10)
        self.sub_port_entry.insert(0, "5500")

        ttk.Label(host_port_section, text="REQ Port:", font=("Poppins", 14)).grid(row=0, column=4, padx=10, pady=10)
        self.req_port_entry = ttk.Entry(host_port_section, font=("Poppins", 12), width=10)
        self.req_port_entry.grid(row=0, column=5, padx=10, pady=10)
        self.req_port_entry.insert(0, "5600")

        # Queue and Supervisor Listboxes
        self.queue_listbox = self.create_listbox(content_frame, "Students in Queue", row=2, column=0)
        self.supervisor_listbox = self.create_listbox(content_frame, "Available Supervisors", row=2, column=1)

        # Status Label for showing connection information
        self.status_label = ttk.Label(content_frame, text="", font=("Poppins", 12), anchor="e")
        self.status_label.grid(row=5, column=0, columnspan=2, sticky="ew", padx=10, pady=10)

        self.logic = QueueLogic(self)

    def create_listbox(self, parent, title, row, column, columnspan=1):
        ttk.Label(parent, text=title, font=("Poppins", 14, "bold")).grid(row=row, column=column, columnspan=columnspan,
                                                                       pady=10)
        listbox = Listbox(parent, height=15, width=40, bg="#f5f5f5", fg="black", selectbackground="#00b09b",
                          selectforeground="white", borderwidth=1, highlightthickness=0, font=("Poppins", 12))
        listbox.grid(row=row + 1, column=column, padx=10, pady=10, columnspan=columnspan, sticky="ew")
        return listbox

    def connect_to_server(self):
        self.logic.connect_to_server()

    def update_queue(self, queue_data):
        self.queue_listbox.delete(0, tk.END)
        for student in queue_data:
            self.queue_listbox.insert(tk.END, student['name'])

    def update_supervisors(self, supervisors_data):
        self.supervisor_listbox.delete(0, tk.END)
        for supervisor in supervisors_data:
            name = supervisor.get('name', '')
            status = supervisor.get('status', '')
            client = supervisor.get('client', '')
            self.supervisor_listbox.insert(tk.END, f"{name} - {status} - {client}")

    def send_heartbeat(self):
        self.logic.send_heartbeat()
        self.after(3000, self.send_heartbeat)


if __name__ == "__main__":
    app = QueueUI()
    app.mainloop()
