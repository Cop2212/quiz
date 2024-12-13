import React from 'react';
import { useNavigate } from 'react-router-dom';
import './IndexPage.css';

const Index = () => {
  const navigate = useNavigate();

  const handleLogin = () => {
    navigate('/login');
  };

  const handleSignup = () => {
    navigate('/signup');
  };

  return (
    <div className="background">
      <div className="container">
        <h1>Welcome to the Quiz App</h1>
        <p>Challenge your knowledge with quizzes designed for everyone!</p>
        <div className="button-group">
          <button className="button login" onClick={handleLogin}>Login</button>
          <button className="button signup" onClick={handleSignup}>Signup</button>
        </div>
      </div>
    </div>
  );
};

export default Index;
