# ClassQueue - Distributed Queue System

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/RaymondSWE/ClassQueue/actions)
[![GitHub stars](https://img.shields.io/github/stars/RaymondSWE/ClassQueue)](https://github.com/RaymondSWE/ClassQueue/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/RaymondSWE/ClassQueue)](https://github.com/RaymondSWE/ClassQueue/network)
[![GitHub license](https://img.shields.io/github/license/RaymondSWE/ClassQueue)](https://github.com/RaymondSWE/ClassQueue/blob/main/LICENSE)

## Introduction
Welcome to the ClassQueue! This system is for a school project and demonstrates a distributed queue with a client-server architecture using ZeroMQ for messaging. The server is built with Java and Spring Boot, and the client is developed in Python using a Tkinter-based GUI. We initially considered using Electron but switched to Python for better native support for ZeroMQ.

## Features
- **Event-Driven Architecture:** Efficient handling of events using ZeroMQ.
- **Multi-Client Support:** Multiple clients can connect and join the queue.
- **Supervisor and Student Roles:** Different functionalities based on user roles.
- **Heartbeat Mechanism:** Maintains queue integrity by checking client activity.

## Project Overview
ClassQueue is a school project that allowed students to pick their languages or frameworks. We chose Spring Boot and Python to demonstrate an event-driven architecture, which is more efficient than constantly streaming data and can earn a High-Performance Mark (HPM).

## Reflection
During development, we maintained a structured workflow with protected main branches, requiring pull requests and code reviews. Manual tests were conducted on each branch before merging PRs. The project was both enjoyable and educational.

## Getting started
You need to have the following installed to run this project:
- Java 17 or later
- Python 3.12 or later
- A Maven environment
  
### Setting Up the server
1. Open a terminal and navigate to the `server` directory at the project's root.
2. Run the following command to install all the dependencies:
```console
mvn install
```
After the dependencies have been installed successfully, use the following command to run the server:
```console
mvn spring-boot:run
```
### Setting Up the client

1. The client directory is located at the root of the project. Open a terminal and navigate to python-client.
2. Run the following command to install the required libraries:
   
```console
pip install -r requirements.txt
```

3. The student and supervisor client is located under python-client/src and can be run using the following command:
   
```console
python student.py
python supervisor_client.py
```

## API Documentation
For detailed API documentation, please refer to the [API Documentation](https://github.com/RaymondSWE/ClassQueue/blob/main/API_DOCUMENTATION.md).

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch.
3. Make your changes.
4. Submit a pull request.


## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

Thank you for visiting ClassQueue Don't forget to ⭐ this repository if you find it useful.


