
from config.server_handler import ServerHandler
class SupervisorLogic:
    def __init__(self, ui):
        self.ui = ui
        self.server_handler = ServerHandler()

    def listen_for_updates(self):
        queue_data = self.server_handler.check_for_updates()
        if queue_data:
            self.ui.update_queue(queue_data)

    def connect_as_supervisor(self, supervisor_name):
        # Logic to connect as a supervisor.
        message = {"type": "supervisor", "supervisorName": supervisor_name, "addSupervisor": True}
        try:
            response = self.server_handler.send_request(message, self.server_handler.req_socket)
            print("Connect as supervisor response:", response)
        except Exception as e:
            print(f"Error connecting as supervisor: {e}")
