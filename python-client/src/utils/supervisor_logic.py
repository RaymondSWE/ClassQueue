import json
import logging

from config.server_handler import ServerHandler
from error.connection_exceptions import EmptyResponseError
from tkinter import messagebox


class SupervisorLogic:
    def __init__(self, ui):
        self.ui = ui
        host = self.ui.host_entry.get()
        sub_port = self.ui.sub_port_entry.get()
        req_port = self.ui.req_port_entry.get()
        try:
            self.server_handler = ServerHandler(host, sub_port, req_port)
        except ConnectionError:
            messagebox.showerror("Error", "Unable to connect to the server!")
            return

    def connect_as_supervisor(self):
        self.supervisorName = self.ui.name_entry.get()
        message = {"type": "supervisor", "supervisorName": self.supervisorName, "addSupervisor": True}
        try:
            response = self.server_handler.send_request(message, self.server_handler.req_socket)
            jsonResponse = response if isinstance(response, dict) else json.loads(response)
            logging.info("Connect as supervisor response: %s", jsonResponse)
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

        status = response.get("status")
        message = response.get("message")

        if status == "error":
            messagebox.showerror("Error", message)
        else:
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

        if status == "error":
            messagebox.showerror("Error", message)
        else:
            messagebox.showinfo("Success", message)
