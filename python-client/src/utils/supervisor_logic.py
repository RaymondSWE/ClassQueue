
from config.server_handler import ServerHandler
class SupervisorLogic:
    def __init__(self, ui):
        self.ui = ui
        self.server_handler = ServerHandler()

    def listen_for_updates(self):
        queue_data = self.server_handler.check_for_updates()
        if queue_data:
            self.ui.update_queue(queue_data)
