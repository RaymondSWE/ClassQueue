import React, { useState, useEffect } from "react";
import "./StudentClient.css";
import { Button } from "../Button/Button";
import { v4 as uuidv4 } from 'uuid'; 
import { toast } from 'react-hot-toast';
import { User } from '../../types/User';


export const StudentClient: React.FC = () => {
  const [name, setName] = useState<string>("");
  const [queue, setQueue] = useState<User[]>([]);
  const ITEMS_PER_PAGE = 5;
  const [currentPage, setCurrentPage] = useState<number>(1);
  const indexOfLastStudent = currentPage * ITEMS_PER_PAGE;
  const indexOfFirstStudent = indexOfLastStudent - ITEMS_PER_PAGE;
  const currentStudents = queue.slice(indexOfFirstStudent, indexOfLastStudent);

  const handleNext = () => {
    if (currentPage < Math.ceil(queue.length / ITEMS_PER_PAGE)) {
      setCurrentPage(currentPage + 1);
    }
  };

  const handlePrev = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  //mock for design
  useEffect(() => {
    const initialQueue = [
      { name: "Raman" },
      { name: "Nibar" },
      { name: "test2" },
      { name: "test4" },
      { name: "tes6" },
      { name: "dsdas" },
      { name: "zxzxz" },
      { name: "bvbv" },
      { name: "tyt" },
      { name: "Nibar" },
      { name: "hghg" },
      { name: "Nibar" },
      { name: "nbnb" },
      { name: "Nibar" },
    ];
    setQueue(initialQueue);
  }, []);

  const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setName(e.target.value);
  };

  const handleJoinQueue = () => {
    const clientId = uuidv4();

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
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        toast.success('Successfully joined the queue!');  
      }).catch((error) => {
        console.error(error);
        toast.error('Failed to join the queue.');  
      });


    setQueue([...queue, { name }]);
    setName("");
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
        <Button
          text="Join Queue"
          className="queue-button"
          onClick={handleJoinQueue}
        />
      </div>
      <div className="queue-section">
        <div className="queue-header">
          <h2>Students in Queue</h2>
          <div className="pagination-controls">
            <button onClick={handlePrev} disabled={currentPage === 1}>
              &lt;
            </button>
            <button
              onClick={handleNext}
              disabled={
                currentPage === Math.ceil(queue.length / ITEMS_PER_PAGE)
              }
            >
              &gt;
            </button>
          </div>
        </div>
        <ul>
          {currentStudents.map((student, index) => (
            <li key={index}>{student.name}</li>
          ))}
        </ul>
      </div>
    </div>
  );
};