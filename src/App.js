import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Index from './components/Index';
import Home from './components/Home';
import Login from './components/Login';
import Signup from './components/Signup';
import TakeQuizPage from './components/TakeQuizPage'; // Trang làm bài

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Index />} />
        <Route path="/home/*" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/take-quiz/:quizId" element={<TakeQuizPage />} />
      </Routes>
    </Router>
  );
}

export default App;
