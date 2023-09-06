import React, { useState, useEffect } from 'react';
import './StudentClient.css';
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


  return (
    <div className="student-client">
    <div className="input-section">
      <div className="input-group">  
        <input 
          type="text" 
          placeholder="Enter your name" 
          value={name} 
        />
      </div>
      <Button text="Join Queue" className='queue-button' />
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
