from app.supervisor_ui import SupervisorUI
from utils.supervisor_logic import SupervisorLogic


class SupervisorClient(SupervisorUI):
    def __init__(self):
        super().__init__()

        self.logic = SupervisorLogic(self)


        # Start listening for updates immediately
       # self.listen_for_updates()

    def listen_for_updates(self):
        self.logic.listen_for_updates()
        self.after(100, self.listen_for_updates)

if __name__ == "__main__":
    app = SupervisorClient()
    app.mainloop()
