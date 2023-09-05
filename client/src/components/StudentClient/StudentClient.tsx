import React, { useState, useEffect } from 'react';
import './StudentClient.css'
import { Button } from '../Button/Button';
interface Student {
  name: string;
}

export const StudentClient: React.FC = () => {
  const [name, setName] = useState<string>('');
  const [queue, setQueue] = useState<Student[]>([]);

    //mock for design
  useEffect(() => {
    const initialQueue = [
      { name: 'Raman' },
      { name: 'Nibar' }
    ];
    setQueue(initialQueue);
  }, []);

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
        <input 
          type="text" 
          placeholder="Enter your name" 
          value={name} 
          onChange={handleNameChange} 
        />
        <Button text= "Join Queue" />
      </div>
      <div className="queue-section">
        <h2>Students in Queue:</h2>
        <ul>
          {queue.map((student, index) => (
            <li key={index}>{student.name}</li>
          ))}
        </ul>
      </div>
    </div>
  );
};
