import React, { useState, useEffect } from "react";
import "./App.css";
import Navbar from "./components/Navbar/Navbar";
import { StudentClient } from "./components/StudentClient/StudentClient";
import { Login } from "./components/Login/Login";
import { Toaster, toast } from 'react-hot-toast';
const { ipcRenderer } = window.require('electron');

function App() {
  const [zmqMessages, setZmqMessages] = useState<string[]>([]);

  useEffect(() => {
    ipcRenderer.on('zmq-message', (_event: any, message: string) => {
      setZmqMessages(prev => [...prev, message]);
      // Notifying via toast
      toast(`New ZMQ message: ${message}`);
    });

    return () => {
      ipcRenderer.removeAllListeners('zmq-message');
    };
  }, []);

  return (
    <>
      <Toaster />
      <Navbar role="STUDENT" />
      {/* <Login /> */}
      <StudentClient />
      <div className="zmq-messages">
        <h3>ZeroMQ Messages:</h3>
        <ul>
          {zmqMessages.map((message, index) => (
            <li key={index}>{message}</li>
          ))}
        </ul>
      </div>
    </>
  );
}

export default App;
