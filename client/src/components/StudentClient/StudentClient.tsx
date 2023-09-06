import React, { useState, useEffect } from 'react';
import './StudentClient.css'
import { Button } from '../Button/Button';
import { Student, Supervisor } from '../../types/StudentClientTypes';
import { createZmqSubscriber } from './reqSubscriber';



export const StudentClient: React.FC = () => {
  const [name, setName] = useState<string>('');
  const [queue, setQueue] = useState<Student[]>([]);
  const [supervisors, setSupervisors] = useState<Supervisor[]>([]);
  const [notification, setNotification] = useState<string | null>(null);

  useEffect(() => {
    const initialQueue = [
      { name: 'Raman' },
      { name: 'Nibar' }
    ];
    setQueue(initialQueue);
  }, []);
  useEffect(() => {
    if (!name) return;  // Guard clauses

    const subscriber = createZmqSubscriber(name, setQueue, setNotification);
    console.log("ZMQ subscriber created:", subscriber);

    return () => {
        // Clean up the subscriber when the component unmounts.
        subscriber.disconnect("tcp://ds.iit.his.se:5555");
        subscriber.close();
    };
}, [name]);


  const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setName(e.target.value);
  };


  const handleJoinQueue = () => {
    setQueue([...queue, { name }]);
    setName('');
    
  };

  return (
    <div className="student-client">
    <div className="input-section">
      <div className="input-group">  
        <input
          type="text"
          placeholder="Server:Port"
        />
        <input 
          type="text" 
          placeholder="Enter your name" 
          value={name} 
          onChange={handleNameChange} 
        />
      </div>
      <Button text="Join Queue" className='queue-button' onClick={handleJoinQueue} />
    </div>
      <div className="queue-section">
        <h2>Students in Queue:</h2>
        <ul>
          {queue.map((student, index) => (
            <li key={index}>
              {student.name} {student.ticket && `(Ticket: ${student.ticket})`}
            </li>
          ))}
        </ul>
      </div>
      <div className="supervisors-section">
        <h2>Available Supervisors:</h2>
        <ul>
          {supervisors.map((supervisor, index) => (
            <li key={index}>
              {supervisor.name} - {supervisor.status}
              {supervisor.client && ` assisting ${supervisor.client.name}`}
            </li>
          ))}
        </ul>
      </div>
      {notification && (
        <div className="notification-section">
          {notification}
        </div>
      )}
    </div>
  );  
};
