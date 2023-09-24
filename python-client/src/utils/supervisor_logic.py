import json
import logging

from config.server_handler import ServerHandler
from error.connection_exceptions import EmptyResponseError

class SupervisorLogic:
    def __init__(self, ui):
        self.ui = ui
        self.server_handler = ServerHandler()
        logging.basicConfig(level=logging.INFO)

    ## Kinda spagetti code in connect_as supervisor fix it in the future
    def connect_as_supervisor(self, supervisor_name):
        message = {"type": "supervisor", "supervisorName": supervisor_name, "addSupervisor": True}
        try:
            response = self.server_handler.send_request(message, self.server_handler.req_socket)
            jsonResponse = response if isinstance(response, dict) else json.loads(response)
            logging.info("Connect as supervisor response: %s", jsonResponse)
        except json.JSONDecodeError:
            logging.error("Received non-JSON response from server: %s", response)
        except EmptyResponseError as se:
            logging.error("Specific error occurred while connecting as supervisor: %s", se)
        except Exception as e:
            logging.error("Unexpected error occurred while connecting as supervisor: %s", e)

    def listen_for_updates(self):
        supervisor_data = self.server_handler.check_for_updates()
        if supervisor_data:
            logging.info("Received Supervisor Data: %s", supervisor_data)
            self.ui.update_supervisor_queue(supervisor_data)