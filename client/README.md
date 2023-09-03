# DistributedQueue Client Application

This is the client application for DistributedQueue built with React and Electron. Follow the setup instructions below to get started.

## Requirements

- Node.js
- npm

### For WSL2 (Windows Subsystem for Linux) Users

- VcXsrv or any X server software

## Setup Instructions

### Windows Users

1. **Clone the Repository**

    ```bash
    git clone https://github.com/RaymondSWE/DistributedQueue/tree/main
    cd DistributedQueue/client
    ```

2. **Install Dependencies**

    ```bash
    npm install
    ```

3. **Run the Application**

    ```bash
    npm run electron-dev
    ```

### Ubuntu Users

1. **Clone the Repository**

    ```bash
    git clone https://github.com/RaymondSWE/DistributedQueue/tree/main
    cd DistributedQueue/client
    ```

2. **Install Dependencies**

    ```bash
    npm install
    ```

3. **Run the Application**

    ```bash
    npm run electron-dev
    ```

### WSL2 Users

1. **Install an X Server on Windows**: A recommended choice is [VcXsrv](https://sourceforge.net/projects/vcxsrv/). Download and install it.

2. **Launch VcXsrv**: After installing, launch XLaunch from the start menu. Use default settings and on the final page, for testing purposes, check the box "Disable access control". Finish the setup.

3. **Set up the `DISPLAY` variable in WSL2**: In your WSL2 terminal, add the following:

    ```bash
    echo 'export DISPLAY=$(awk '/nameserver / {print $2; exit}' /etc/resolv.conf 2>/dev/null):0' >> ~/.bashrc
    source ~/.bashrc
    ```

4. **Clone the Repository**

    ```bash
    git clone https://github.com/RaymondSWE/DistributedQueue/tree/main
    cd DistributedQueue/client
    ```

5. **Install Dependencies**

    ```bash
    npm install
    ```

6. **Run the Application**

    ```bash
    npm run electron-dev
    ```

## Contributing

If you'd like to contribute, please fork the repository, make your changes, and open a pull request. We greatly appreciate your help!

## Issues

If you encounter any issues, please open an issue on the GitHub repository.

## License

This project is licensed under the MIT License.
