import { setupZmqListeners } from './services/zmqService';
const { app, BrowserWindow, ipcMain } = require('electron');
const isDev = require('electron-is-dev');
const path = require('path');

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

function createWindow(config) {
    const win = new BrowserWindow({
        title: config.title,
        width: config.width,
        height: config.height,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false
        }
    });

    win.loadURL(
        isDev
            ? `http://localhost:3000${config.urlPath}`
            : `file://${path.join(__dirname, `../build${config.urlPath}.html`)}`
    );

    if (isDev) win.webContents.openDevTools({ mode: 'detach' });

    if (config.zmqConfig) {
        setupZmqListeners(win);
    }
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
