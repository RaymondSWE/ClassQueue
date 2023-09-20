import tkinter as tk
from tkinter import ttk, Listbox
from ttkthemes import ThemedTk

class QueueUI(ThemedTk):
    def __init__(self):
        super().__init__()

        self.set_theme("adapta")
        self.title("Student Client")
        self.geometry("700x550")

        # frame
        content_frame = ttk.Frame(self)
        content_frame.pack(fill=tk.BOTH, expand=True, padx=30, pady=30)

        # outer frame
        outer_frame = ttk.Frame(content_frame)
        outer_frame.grid(row=0, column=0, sticky="nsew", padx=20, pady=20)

        # Input section
        input_section = ttk.Frame(outer_frame)
        input_section.grid(row=0, column=0, columnspan=2, sticky="ew", pady=20)
        ttk.Label(input_section, text="Name:", font=("Poppins", 12)).grid(row=0, column=0, padx=10, pady=10)
        self.name_entry = ttk.Entry(input_section, font=("Poppins", 12), width=30)
        self.name_entry.grid(row=0, column=1, padx=10, pady=10)
        self.join_queue_button = ttk.Button(input_section, text="Join Queue")
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
        ttk.Label(supervisor_section, text="Available Supervisors", font=("Poppins", 14, "bold")).grid(row=0, column=0, pady=10)
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

    def update_supervisor(self, supervisors):
        self.supervisor_listbox.delete(0, tk.END)
        for supervisor in supervisors:
            if supervisor["student"]:
                display_text = f"{supervisor['name']} - {supervisor['status']} (Helping: {supervisor['student']})"
            else:
                display_text = f"{supervisor['name']} - {supervisor['status']}"
            self.supervisor_listbox.insert(tk.END, display_text)


if __name__ == "__main__":
    app = QueueUI()
    app.mainloop()
