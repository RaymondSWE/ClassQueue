from app.queue_ui import QueueUI
from utils.queue_logic import QueueLogic

class QueueClient(QueueUI):
    def __init__(self):
        super().__init__()
        self.logic = QueueLogic(self)

        # Set the command for the button
        self.join_queue_button.config(command=self.logic.join_queue)

        # Listenes for updates every 1 sec, sends heartsbeats every minute
        self.after(1000, self.listen_for_updates)
        self.after(60000, self.send_heartbeat)

    def send_heartbeat(self):
        self.logic.send_heartbeat()
        self.after(3000, self.send_heartbeat)

    def listen_for_updates(self):
        self.logic.listen_for_updates()
        self.after(1000, self.listen_for_updates)


if __name__ == "__main__":
    app = QueueClient()
    app.mainloop()
