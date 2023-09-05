import React from 'react';
import './Button.css';

interface ButtonProps {
  onClick?: () => void;
  text: string;
  className?: string;
  styleType?: 'primary' | 'secondary';

}

export const Button: React.FC<ButtonProps> = ({ onClick, text, className, styleType = 'primary' }) => {
    return (
      <button onClick={onClick} className={`custom-button ${styleType} ${className}`}>
        {text}
      </button>
    );
  }
  