import json
import logging

from config.server_handler import ServerHandler
from error.connection_exceptions import EmptyResponseError
from tkinter import messagebox
import tkinter as tk


class SupervisorLogic:
    def __init__(self, ui):
        self.ui = ui
        self.host = "localhost"
        self.sub_port = "5500"
        self.req_port = "5600"

    def connect_to_server(self):
        try:
            self.server_handler = ServerHandler(self.host, self.sub_port, self.req_port)
            connected = self.server_handler.connect()
            if connected:
                messagebox.showinfo("Success", f"Connected to the server at {self.host} successfully!")
                self.ui.connect_button['state'] = tk.NORMAL
                self.ui.attend_student_button['state'] = tk.NORMAL
                self.ui.status_label.config(
                    text=f"Connected to {self.host}: SUB Port - {self.sub_port}, REQ Port - {self.req_port}")
                self.ui.listen_for_updates()
            else:
                logging.error("Unable to connect to the server! (╯༎ຶ ۝ ༎ຶ）╯︵ ┻━┻")
                messagebox.showerror("Error", "Unable to connect to the server!")
                self.ui.status_label.config(text="Unable to connect!")
                self.ui.quit()
        except Exception as e:
            logging.error(f"Error connecting to the server: {e}")
            messagebox.showerror("Error", f"Error connecting to the server: {e}")
            self.ui.quit()

    def connect_as_supervisor(self):
        self.supervisorName = self.ui.name_entry.get()
        message = {"type": "supervisor", "supervisorName": self.supervisorName, "addSupervisor": True}
        try:
            response = self.server_handler.send_request(message, self.server_handler.req_socket)
            jsonResponse = response if isinstance(response, dict) else json.loads(response)
            logging.info("Connect as supervisor response: %s", jsonResponse)
            self.ui.listen_for_updates()
        except json.JSONDecodeError:
            logging.error("Received non-JSON response from server: %s", response)
        except EmptyResponseError as se:
            logging.error("Specific error occurred while connecting as supervisor: %s", se)
        except Exception as e:
            logging.error("Unexpected error occurred while connecting as supervisor: %s", e)  # attend the queue

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
            self.ui.listen_for_updates()

    def listen_for_updates(self):
        update = self.server_handler.check_for_updates()
        if update:
            topic, data = update
            if topic == "supervisors":
                self.ui.update_supervisor_queue(data)
            elif topic == "queue":
                self.ui.update_queue(data)
            elif topic == "error":
                messagebox.showerror("Error", data.get("message"))

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
            self.ui.listen_for_updates()
