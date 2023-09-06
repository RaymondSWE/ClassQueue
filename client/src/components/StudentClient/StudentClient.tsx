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
    // Use a random client ID for the example, but you'd use something unique
    const clientId = Math.random().toString(36).substring(7);
  
    fetch("http://localhost:8080/joinQueue", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: name,
        clientId: clientId,
      }),
    })
    .then(response => response.json())
    .then(data => {
      console.log(data);
    });
  
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
        <Button text= "Join Queue" onClick={handleJoinQueue} />
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
