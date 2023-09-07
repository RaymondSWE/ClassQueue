import React, { useEffect } from "react";
import { toast } from 'react-hot-toast';
import useZmqListener from '../../hooks/useZmqListener';
import './ZmqMessageList.css';

const ZmqMessageList: React.FC = () => {
    const zmqMessages = useZmqListener();

    useEffect(() => {
        zmqMessages.forEach(message => {
            toast(`New ZMQ message: ${message}`);
        });
    }, [zmqMessages]);

    return (
        <div className="zmq-messages">
            <h3>ZeroMQ Messages</h3>
            <ul>
                {zmqMessages.map((message, index) => (
                    <li key={index}>{message}</li>
                ))}
            </ul>
        </div>
    );
};

export default ZmqMessageList;
