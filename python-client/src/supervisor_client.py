from app.supervisor_ui import SupervisorUI


class SupervisorClient(SupervisorUI):
    def __init__(self):
        super().__init__()

        self.connect_button.config(command=self.connect_to_server)

        self.logic.listen_for_updates()

    def connect_to_server(self):
        supervisor_name = self.name_entry.get()


if __name__ == "__main__":
    app = SupervisorClient()
    app.mainloop()
