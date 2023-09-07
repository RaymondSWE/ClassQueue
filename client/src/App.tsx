import React, { useState, useEffect } from "react";
import "./App.css";
import Navbar from "./components/Navbar/Navbar";
import { StudentClient } from "./components/StudentClient/StudentClient";
import { Login } from "./components/Login/Login";
import { Toaster, toast } from 'react-hot-toast';
import useZmqListener from './hooks/useZmqListener';

function App() {
  const zmqMessages = useZmqListener();

  useEffect(() => {
      zmqMessages.forEach(message => {
          toast(`New ZMQ message: ${message}`);
      });
  }, [zmqMessages]);

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