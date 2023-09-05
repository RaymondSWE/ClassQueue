import React, { useState } from "react";
import "./Navbar.css";
import { faUser, faTools, faProjectDiagram, faEnvelope } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

interface NavbarProps {
  role: "STUDENT" | "SUPERVISOR" | "SERVER" | "ALL";
}

const Navbar: React.FC<NavbarProps> = ({ role }) => {
  const [active, setActive] = useState(false); //adjust in future

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <a href="#hero" className="navbar-container-navbar-logo">
          Queue System
        </a>
        <ul className={`navbar-container-navbar-menu ${active ? "active" : ""}`}>
          {(role === "STUDENT" || role === "ALL") && (
            <li className="navbar-container-navbar-item">
              <a href="#about" className="navbar-container-navbar-links">
                <FontAwesomeIcon icon={faUser} /> Students
              </a>
            </li>
          )}
          {(role === "SUPERVISOR" || role === "ALL") && (
            <li className="navbar-container-navbar-item">
              <a href="#skills" className="navbar-container-navbar-links">
                <FontAwesomeIcon icon={faTools} /> Supervisor
              </a>
            </li>
          )}
          {(role === "SERVER" || role === "ALL") && (
            <li className="navbar-container-navbar-item">
              <a href="#projects" className="navbar-container-navbar-links">
                <FontAwesomeIcon icon={faProjectDiagram} /> Server
              </a>
            </li>
          )}
          <li className="navbar-container-navbar-item">
            <a href="#contact" className="navbar-container-navbar-links">
              <FontAwesomeIcon icon={faEnvelope} /> Contact
            </a>
          </li>
        </ul>
      </div>
    </nav>
  );
};

export default Navbar;
