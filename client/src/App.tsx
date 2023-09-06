import React from "react";
import "./App.css";
import Navbar from "./components/Navbar/Navbar";
import { StudentClient } from "./components/StudentClient/StudentClient";
import { Login } from "./components/Login/Login";

function App() {
  return (
    <>
      <Navbar role="STUDENT" />
      {/* <Login /> */}
      <StudentClient />
    </>
  );
}

export default App;
