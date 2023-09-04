import React, { useEffect, useRef, useState } from "react";
import "./Navbar.css";
import {
  faUser,
  faTools,
  faProjectDiagram,
  faBriefcase,
  faUniversity,
  faEnvelope,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const Navbar = () => {
  const ref = useRef(null);
  const [active, setActive] = useState(false);



  return (
    <nav className="navbar">
      <div className="navbar-container" ref={ref}>
        <a href="#hero" className="navbar-container-navbar-logo">
          Queue System
        </a>
        <ul
          className={`navbar-container-navbar-menu ${active ? "active" : ""}`}
        >
          <li className="navbar-container-navbar-item">
            <a
              href="#about"
              className="navbar-container-navbar-links"
            >
              <FontAwesomeIcon icon={faUser} /> Students
            </a>
          </li>
          <li className="navbar-container-navbar-item">
            <a
              href="#skills"
              className="navbar-container-navbar-links"
            >
              <FontAwesomeIcon icon={faTools} /> Supervisor
            </a>
          </li>
          <li className="navbar-container-navbar-item">
            <a
              href="#projects"
              className="navbar-container-navbar-links"
            >
              <FontAwesomeIcon icon={faProjectDiagram} /> Server
            </a>
          </li>
          <li className="navbar-container-navbar-item">
            <a
              href="#contact"
              className="navbar-container-navbar-links"
            >
              <FontAwesomeIcon icon={faEnvelope} /> Contact
            </a>
          </li>
        </ul>
      </div>
    </nav>
  );
};

export default Navbar;