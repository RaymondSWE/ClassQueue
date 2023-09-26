import json
import logging

from config.server_handler import ServerHandler
from error.connection_exceptions import EmptyResponseError
from tkinter import messagebox


class SupervisorLogic:
    def __init__(self, ui):
        self.ui = ui
        self.server_handler = ServerHandler()
        logging.basicConfig(level=logging.INFO)

    ## Kinda spagetti code in connect_as supervisor fix it in the future
    def connect_as_supervisor(self):
        self.supervisorName = self.ui.name_entry.get()
        message = {"type": "supervisor", "supervisorName": self.supervisorName, "addSupervisor": True}
        try:
            response = self.server_handler.send_request(message, self.server_handler.req_socket)
            jsonResponse = response if isinstance(response, dict) else json.loads(response)
            logging.info("Connect as supervisor response: %s", jsonResponse)
            if "error" in jsonResponse:
                messagebox.showerror("error", jsonResponse.get("message"))
        except json.JSONDecodeError:
            logging.error("Received non-JSON response from server: %s", response)
        except EmptyResponseError as se:
            logging.error("Specific error occurred while connecting as supervisor: %s", se)
        except Exception as e:
            logging.error("Unexpected error occurred while connecting as supervisor: %s", e)  # attend the queue
        # attend the queue

    def attend_queue(self):
        message = self.ui.message_entry.get()
        request = {
            "message": message,
            "type": "supervisor",
            "attendStudent": True,
            "supervisorName": self.supervisorName
        }
        response = self.server_handler.send_request(request, self.server_handler.req_socket)
        logging.info("Attend request sent")
        if "error" in response:
            messagebox.showerror("error", response.get("message"))
            return

        status = response.get("status")
        message = response.get("message")

        if status == "success":
            messagebox.showinfo("Success", message)

    def listen_for_updates(self):
        update = self.server_handler.check_for_updates()
        if update:
            topic, data = update
            if topic == "supervisors":
                self.ui.update_supervisor_queue(data)
            elif topic == "queue":
                self.ui.update_queue(data)

    def make_supervisor_available(self):
        request = {
            "type": "supervisor",
            "makeAvailable": True,
            "supervisorName": self.supervisorName
        }
        response = self.server_handler.send_request(request, self.server_handler.req_socket)
        logging.info("Make available request sent")

        status = response.get("status")
        message = response.get("message")

        if "error" in response:
            messagebox.showerror("Error", message)
        else:
            messagebox.showinfo( message)
