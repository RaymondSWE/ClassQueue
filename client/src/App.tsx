import React from "react";
import "./App.css";
import Navbar from "./components/Navbar/Navbar";
import { StudentClient } from "./components/StudentClient/StudentClient";
import { Login } from "./components/Login/Login";
import { Toaster } from 'react-hot-toast';
import ZmqMessageList from './components/ZmqMessageList/ZmqMessageList';

function App() {
  return (
    <>
      <Toaster />
      <Navbar role="STUDENT" />
      {/* <Login /> */}
      <StudentClient />
      <ZmqMessageList />
    </>
  );
}

export default App;
