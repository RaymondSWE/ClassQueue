const { app, BrowserWindow, ipcMain } = require('electron');
const isDev = require('electron-is-dev');
const path = require('path');
const zmq = require('zeromq');

let mainWindow; // Declare mainWindow variable at the top of your file
const windowsConfigurations = {
    mainMenu: {
        title: "Main Menu",
        width: 1200,
        height: 800,
        urlPath: '/mainmenu',
        zmqConfig: true
    },
    student: {
        title: "Students",
        width: 800,
        height: 600,
        urlPath: '/students'
    },
    supervisor: {
        title: "Supervisor",
        width: 800,
        height: 600,
        urlPath: '/supervisor'
    },
    server: {
        title: "Server",
        width: 800,
        height: 600,
        urlPath: '/server'
    }
};

// Create a ZeroMQ Subscriber socket
let subSocket = zmq.socket('sub');
subSocket.subscribe('queue');
subSocket.connect('tcp://ds.iit.his.se:5555');

// Create a ZeroMQ Request socket
let reqSocket = zmq.socket('req');
reqSocket.connect('tcp://ds.iit.his.se:5556');


// Listen for 'join-queue' event from React
ipcMain.on('join-queue', (event, { name, clientId }) => {
    reqSocket.send(JSON.stringify({
        enterQueue: true,
        name: name,
        clientId: clientId
    }));

    reqSocket.on('message', (response) => {
        response = JSON.parse(response);
        let ticket = response.ticket;
        if (ticket) {
            // Send response to React component
            event.reply('update-queue', ticket);
        } else {
            // Handle error
        }
    });
});

subSocket.on('message', (topic, message) => {
    let students = JSON.parse(message);
    mainWindow.webContents.send('update-queue', students);
});



function createWindow(config) {
    const win = new BrowserWindow({
        title: config.title,
        width: config.width,
        height: config.height,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false,
            enableRemoteModule: true // also set this to true

        }
    });

    win.loadURL(
        isDev
            ? `http://localhost:3000${config.urlPath}`
            : `file://${path.join(__dirname, `../build${config.urlPath}.html`)}`
    );

    if (isDev) win.webContents.openDevTools({ mode: 'detach' });

}



app.whenReady().then(() => {
    createWindow(windowsConfigurations.mainMenu);
    // Uncomment below to start other windows up at startup
    // createWindow(windowsConfigurations.student);
    // createWindow(windowsConfigurations.supervisor);
    // createWindow(windowsConfigurations.server);
});

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit();
    }
});

app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
        Object.values(windowsConfigurations).forEach(config => {
            createWindow(config);
        });
    }
});
