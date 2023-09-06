import React from "react";
import "./Login.css";
import { Button } from "../Button/Button";

export const Login = () => {
  return (
    <div className="login-box">
      <h2>Login</h2>
      <form>
        <div className="user-box">
          <input type="text" name="" />
          <label>Username</label>
        </div>
        <div className="user-box">
          <input type="password" name="" />
          <label>Password</label>
        </div>
        <Button text="Login" />
        <Button text="Register" styleType="secondary" />
      </form>
    </div>
  );
};
