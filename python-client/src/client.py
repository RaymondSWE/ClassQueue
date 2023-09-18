from app.queue_ui import QueueUI
from utils.queue_logic import QueueLogic

class QueueClient(QueueUI):
    def __init__(self):
        super().__init__()
        self.logic = QueueLogic(self)
        self.logic.server_handler.send_startup_message()


        # Set the command for the button
        self.join_queue_button.config(command=self.logic.join_queue)

        # Start listening for updates immediately
        self.listen_for_updates()

    def listen_for_updates(self):
        self.logic.listen_for_updates()
        self.after(100, self.listen_for_updates)


if __name__ == "__main__":
    app = QueueClient()
    app.mainloop()
