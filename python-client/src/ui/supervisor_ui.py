import tkinter as tk
from tkinter import ttk, Listbox, PhotoImage
from ttkthemes import ThemedTk
from utils.supervisor_logic import SupervisorLogic


class SupervisorUI(ThemedTk):
    def __init__(self):
        super().__init__()

        self.set_theme("equilux")
        self.title("Supervisor Client")
        self.geometry("1000x600")

        self.user_icon = PhotoImage(file='assets/user.png').subsample(20, 20)
        self.connect_icon = PhotoImage(file='assets/server-connection.png').subsample(15, 15)
        self.attend_icon = PhotoImage(file='assets/attend.png').subsample(15, 15)
        self.available_icon = PhotoImage(file='assets/available.png').subsample(10, 15)
        self.supervisor_icon = PhotoImage(file='assets/supervisor_transparent.png').subsample(15, 15)
        self.message_icon = PhotoImage(file='assets/message.png').subsample(20, 20)

        self.style = ttk.Style()
        self.style.configure('.', padding=5, foreground='#ffffff')

        # Main frame
        content_frame = ttk.Frame(self)
        content_frame.pack(fill=tk.BOTH, expand=True)

        # Name input
        ttk.Label(content_frame, text="Supervisor Name:", image=self.user_icon, compound="left",
                  font=("Poppins", 14)).grid(row=0, column=0, padx=10, pady=10, sticky="w")
        self.name_entry = ttk.Entry(content_frame, font=("Poppins", 12), width=30)
        self.name_entry.grid(row=0, column=1, padx=10, pady=10, sticky="w")

        # Message input
        ttk.Label(content_frame, text="Message:", image=self.message_icon, compound="left", font=("Poppins", 14)).grid(
            row=1, column=0, padx=10, pady=10,
            sticky="w")
        self.message_entry = ttk.Entry(content_frame, font=("Poppins", 12), width=30)
        self.message_entry.grid(row=1, column=1, padx=10, pady=10, sticky="w")

        # Button to connect as supervisor
        self.connect_button = ttk.Button(content_frame, text="Connect as Supervisor", image=self.supervisor_icon,
                                         compound="left", command=self.connect_as_supervisor, state=tk.DISABLED)
        self.connect_button.grid(row=1, column=2, padx=10, pady=10, sticky="w")

        # Connect to server button
        self.connect_server_button = ttk.Button(content_frame, text="Connect to Server", image=self.connect_icon,
                                                compound="left", command=self.connect_to_server)
        self.connect_server_button.grid(row=0, column=2, padx=10, pady=10, sticky="w")

        # Attend student button
        self.attend_student_button = ttk.Button(content_frame, text="Attend Student", image=self.attend_icon,
                                                compound="left", command=self.attend_queue, state=tk.DISABLED)
        self.attend_student_button.grid(row=0, column=3, padx=10, pady=10, sticky="w")

        # Make available button
        self.available_button = ttk.Button(content_frame, text="Available", image=self.available_icon,
                                           command=self.make_available)
        self.available_button.grid(row=0, column=4, padx=10, pady=10, sticky="w")

        # Listboxes
        self.queue_listbox = self.create_listbox(content_frame, "Students in Queue", row=2, column=0, columnspan=2)
        self.supervisor_listbox = self.create_listbox(content_frame, "Connected Supervisors", row=2, column=2,
                                                      columnspan=3)

        # Status Label for showing connection information
        self.status_label = ttk.Label(content_frame, text="", image='', compound="left",
                                      font=("Poppins", 12), anchor="e", foreground="#ffffff")
        self.status_label.grid(row=4, column=0, columnspan=5, sticky="ew", padx=10, pady=10)

        self.logic = SupervisorLogic(self)

    def create_listbox(self, parent, title, row, column, columnspan=1):
        ttk.Label(parent, text=title, font=("Poppins", 14, "bold"), foreground="#ffffff").grid(row=row, column=column,
                                                                                               columnspan=columnspan,
                                                                                               pady=10)
        listbox = Listbox(parent, height=15, width=40, bg="#333333", fg="#ffffff", selectbackground="#00b09b",
                          selectforeground="white", borderwidth=1, highlightthickness=0, font=("Poppins", 12))
        listbox.grid(row=row + 1, column=column, columnspan=columnspan, sticky="ew", padx=(50, 0))
        return listbox

    def connect_to_server(self):
        self.logic.connect_to_server()

    def connect_as_supervisor(self):
        self.logic.connect_as_supervisor()

    def attend_queue(self):
        self.logic.attend_queue()

    def update_supervisor_queue(self, supervisor_data):
        self.supervisor_listbox.delete(0, tk.END)
        for supervisor in supervisor_data:
            display_text = f"{supervisor['name']} - {supervisor['status']}"
            self.supervisor_listbox.insert(tk.END, display_text)

    def update_queue(self, queue_data):
        self.queue_listbox.delete(0, tk.END)
        for index, student in enumerate(queue_data, start=1):
            display_text = f"{index}. {student['name']}"
            self.queue_listbox.insert(tk.END, display_text)

    def make_available(self):
        self.logic.make_supervisor_available()


if __name__ == "__main__":
    app = SupervisorUI()
    app.mainloop()
