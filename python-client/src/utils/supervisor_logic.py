from config.server_handler import ServerHandler


class SupervisorLogic:
    def __init__(self, ui):
        self.ui = ui
        self.server_handler = ServerHandler()

    def connect_as_supervisor(self, supervisor_name):
        # Logic to connect as a supervisor.
        message = {"type": "supervisor", "supervisorName": supervisor_name, "addSupervisor": True}
        try:
            response = self.server_handler.send_request(message, self.server_handler.req_socket)
            print("Connect as supervisor response:", response)
        except Exception as e:
            print(f"Error connecting as supervisor: {e}")
