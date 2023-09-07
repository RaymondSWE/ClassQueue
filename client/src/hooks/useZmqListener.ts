// hooks/useZmqListener.ts
import { useState, useEffect } from "react";
const { ipcRenderer } = window.require('electron');

const useZmqListener = () => {
    const [zmqMessages, setZmqMessages] = useState<string[]>([]);

    useEffect(() => {
        ipcRenderer.on('zmq-message', (_event: any, message: string) => {
            setZmqMessages(prev => [...prev, message]);
        });

        return () => {
            ipcRenderer.removeAllListeners('zmq-message');
        };
    }, []);

    return zmqMessages;
};

export default useZmqListener;
