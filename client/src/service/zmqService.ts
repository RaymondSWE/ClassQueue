
import { BrowserWindow, ipcMain } from 'electron';
import zmq from 'zeromq';

export async function setupZmqListeners(win: BrowserWindow): Promise<void> {
    const subscriber = new zmq.Subscriber();
    subscriber.connect('tcp://ds.iit.his.se:5555');
    subscriber.subscribe('queue');
    
    (async () => {
        for await (const [topic, message] of subscriber) {
            win.webContents.send('zmq-message', message.toString());
            console.log(message.toString());
        }
    })();

    const requester = new zmq.Request();
    requester.connect('tcp://ds.iit.his.se:5556');

    (async () => {
        for await (const [message] of requester) {
            win.webContents.send('zmq-response', message.toString());
        }
    })();
    console.log("[Renderer] Sending join-queue message to main process.");


    ipcMain.on('join-queue', async (event, data) => {
        console.log('[Main Process] Received join-queue event:', data);
        await requester.send(JSON.stringify(data));
    });
    
    

}

