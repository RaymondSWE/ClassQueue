import React from 'react';
import './App.css';
import Navbar from './components/Navbar/Navbar';
import { StudentClient } from './components/StudentClient/StudentClient';

function App() {
  return (
    < >
      <Navbar/>
      <StudentClient />
    </>
  );
}

export default App;
