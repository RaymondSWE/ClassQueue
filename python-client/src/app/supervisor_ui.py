import logging
import tkinter as tk
from tkinter import ttk, Listbox
from ttkthemes import ThemedTk
from utils.supervisor_logic import SupervisorLogic


class SupervisorUI(ThemedTk):
    def __init__(self):
        super().__init__()

        self.set_theme("adapta")
        self.title("Supervisor Client")
        self.geometry("800x600")

        # Main frame
        content_frame = ttk.Frame(self)
        content_frame.pack(fill=tk.BOTH, expand=True, padx=20, pady=20)

        # Input section
        ttk.Label(content_frame, text="Supervisor Name:", font=("Arial", 14)).grid(row=0, column=0, padx=10, pady=10,
                                                                                   sticky="e")
        self.name_entry = ttk.Entry(content_frame, font=("Arial", 12), width=40)
        self.name_entry.grid(row=0, column=1, padx=10, pady=10, columnspan=2)
        self.connect_button = ttk.Button(content_frame, text="Connect", command=self.connect_as_supervisor)
        self.connect_button.grid(row=0, column=3, padx=10, pady=10)

        # Message entry and action
        ttk.Label(content_frame, text="Message:", font=("Arial", 14)).grid(row=1, column=0, padx=10, pady=10,
                                                                           sticky="e")
        self.message_entry = ttk.Entry(content_frame, font=("Arial", 12), width=40)
        self.message_entry.grid(row=1, column=1, padx=10, pady=10, columnspan=2)
        self.attend_student_button = ttk.Button(content_frame, text="Attend Student")
        self.attend_student_button.grid(row=1, column=3, padx=10, pady=10)

        # Queue and supervisors
        self.queue_listbox = self._create_listbox(content_frame, "Students in Queue", row=2, column=0, columnspan=2)
        self.supervisor_listbox = self._create_listbox(content_frame, "Connected Supervisors", row=2, column=2,
                                                       columnspan=2)

        self.logic = SupervisorLogic(self)

    def _create_listbox(self, parent, title, row, column, columnspan=1):
        ttk.Label(parent, text=title, font=("Arial", 14, "bold")).grid(row=row, column=column, columnspan=columnspan,
                                                                       pady=10)
        listbox = Listbox(parent, height=15, width=40, bg="#f5f5f5", fg="black",
                          selectbackground="#00b09b", selectforeground="white", borderwidth=1,
                          highlightthickness=0, font=("Arial", 12))
        listbox.grid(row=row + 1, column=column, padx=10, pady=10, columnspan=columnspan)
        return listbox

    def connect_as_supervisor(self):
        supervisor_name = self.name_entry.get()
        if supervisor_name:
            self.logic.connect_as_supervisor(supervisor_name)

    def update_supervisor_queue(self, supervisor_data):
        self.supervisor_listbox.delete(0, tk.END)
        for supervisor in supervisor_data:
            display_text = f"{supervisor['name']} - {supervisor['status']}"
            self.supervisor_listbox.insert(tk.END, display_text)


if __name__ == "__main__":
    app = SupervisorUI()
    app.mainloop()
