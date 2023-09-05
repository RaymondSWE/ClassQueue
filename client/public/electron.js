const { app, BrowserWindow } = require('electron');
const isDev = require('electron-is-dev');
const path = require('path');

function createMainMenuWindow() {
    const win = new BrowserWindow({
        title: "Main Menu",
        width: 1200,
        height: 800,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false,
        },
    });

    win.loadURL(
        isDev
            ? 'http://localhost:3000/mainmenu'
            : `file://${path.join(__dirname, '../build/mainmenu.html')}`
    );

    if (isDev) win.webContents.openDevTools({ mode: 'detach' });
}

function createStudentWindow() {
    const win = new BrowserWindow({
        title: "Students",
        width: 800,
        height: 600,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false,
        },
    });

    win.loadURL(
        isDev
            ? 'http://localhost:3000/students'
            : `file://${path.join(__dirname, '../build/students.html')}`
    );

    if (isDev) win.webContents.openDevTools({ mode: 'detach' });
}

function createSupervisorWindow() {
    const win = new BrowserWindow({
        title: "Supervisor",
        width: 800,
        height: 600,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false,
        },
    });

    win.loadURL(
        isDev
            ? 'http://localhost:3000/supervisor'
            : `file://${path.join(__dirname, '../build/supervisor.html')}`
    );

    if (isDev) win.webContents.openDevTools({ mode: 'detach' });
}

function createServerWindow() {
    const win = new BrowserWindow({
        title: "Server",
        width: 800,
        height: 600,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false,
        },
    });

    win.loadURL(
        isDev
            ? 'http://localhost:3000/server'
            : `file://${path.join(__dirname, '../build/server.html')}`
    );

    if (isDev) win.webContents.openDevTools({ mode: 'detach' });
}

app.whenReady().then(() => {
    createMainMenuWindow();
    // createStudentWindow();
    // createSupervisorWindow();
    // createServerWindow();
});

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit();
    }
});

app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
        createMainMenuWindow();
        createStudentWindow();
        createSupervisorWindow();
        createServerWindow();
    }
});
