from app.supervisor_ui import SupervisorUI


class SupervisorClient(SupervisorUI):
    def __init__(self):
        super().__init__()

        self.logic.listen_for_updates()


if __name__ == "__main__":
    app = SupervisorClient()
    app.mainloop()
