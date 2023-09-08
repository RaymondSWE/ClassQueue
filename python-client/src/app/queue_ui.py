import tkinter as tk
from tkinter import ttk, Listbox, messagebox


class QueueUI(tk.Tk):
    def __init__(self):
        super().__init__()

        self.title("Queue Client")
        self.geometry("400x300")

        self.style = ttk.Style()
        self.style.configure("TButton", background="#00b09b")

        self.create_widgets()

    def create_widgets(self):
        self.input_section = ttk.Frame(self, padding="20")
        self.input_section.grid(row=0, column=0, sticky="ew")

        self.name_entry = ttk.Entry(self.input_section)
        self.name_entry.grid(row=0, column=0, sticky="ew", padx=5)

        self.join_queue_button = ttk.Button(self.input_section, text="Join Queue")
        # Command will be set later from client.py
        self.join_queue_button.grid(row=0, column=1, padx=5)

        self.queue_section = ttk.Frame(self, padding="20")
        self.queue_section.grid(row=1, column=0, sticky="ew")

        self.queue_label = ttk.Label(self.queue_section, text="Students in Queue")
        self.queue_label.grid(row=0, column=0, columnspan=2)

        self.queue_listbox = Listbox(self.queue_section, height=10)
        self.queue_listbox.grid(row=1, column=0, columnspan=2)

    def update_queue(self, students):
        self.queue_listbox.delete(0, tk.END)
        for student in students:
            self.queue_listbox.insert(tk.END, student['name'])
