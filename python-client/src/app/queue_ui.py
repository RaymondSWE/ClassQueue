import tkinter as tk
from tkinter import ttk, Listbox
from ttkthemes import ThemedTk

class QueueUI(ThemedTk):
    def __init__(self):
        super().__init__()

        self.set_theme("plastik")
        self.title("Queue Client")
        self.geometry("400x300")

        # Create a frame for the content
        content_frame = ttk.Frame(self)
        content_frame.pack(fill=tk.BOTH, expand=True)

        # Create an outer frame with padding
        outer_frame = ttk.Frame(content_frame, padding=20)
        outer_frame.grid(row=0, column=0, sticky="nsew")

        # Input section
        input_section = ttk.Frame(outer_frame, style="TFrame")
        input_section.grid(row=0, column=0, sticky="ew", pady=10)

        ttk.Label(input_section, text="Name:", style="TLabel").grid(row=0, column=0, padx=5, pady=5)
        self.name_entry = ttk.Entry(input_section, style="TEntry")
        self.name_entry.grid(row=0, column=1, padx=5)
        self.join_queue_button = ttk.Button(input_section, text="Join Queue", style="TButton")
        self.join_queue_button.grid(row=0, column=2, padx=5)

        # Queue section
        queue_section = ttk.Frame(outer_frame, style="TFrame")
        queue_section.grid(row=1, column=0, sticky="nsew")

        ttk.Label(queue_section, text="Students in Queue", style="TLabel").grid(row=0, column=0, columnspan=2)
        self.queue_listbox = Listbox(queue_section, height=10, bg="#555555", fg="white",
                                     selectbackground="#00b09b", selectforeground="white", borderwidth=0,
                                     highlightthickness=0)
        self.queue_listbox.grid(row=1, column=0, columnspan=2, pady=10)

        outer_frame.columnconfigure(0, weight=1)
        outer_frame.rowconfigure(0, weight=1)
        outer_frame.rowconfigure(1, weight=1)

    def update_queue(self, students):
        self.queue_listbox.delete(0, tk.END)
        for student in students:
            self.queue_listbox.insert(tk.END, student['name'])

if __name__ == "__main__":
    app = QueueUI()
    app.mainloop()
