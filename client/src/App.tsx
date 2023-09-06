import React from "react";
import "./App.css";
import Navbar from "./components/Navbar/Navbar";
import { StudentClient } from "./components/StudentClient/StudentClient";
import { Login } from "./components/Login/Login";
import { Toaster } from 'react-hot-toast';

function App() {
  return (
    <>
      <Toaster />  
      <Navbar role="STUDENT" />
      {/* <Login /> */}
      <StudentClient />
    </>
  );
}

export default App;
