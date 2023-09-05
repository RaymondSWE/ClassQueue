export interface Student {
    name: string;
    ticket?: number;
  }
  
  export interface Supervisor {
    name: string;
    status: 'pending' | 'available' | 'occupied';
    client?: Student;
  }
  