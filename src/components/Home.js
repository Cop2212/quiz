import React, { useEffect, useState } from 'react';
import { Routes, Route, NavLink, useNavigate } from 'react-router-dom';  // Dùng useNavigate thay vì useHistory
import './Home.css';
import HomePage from './HomePage';
import CreateQuizPage from './CreateQuizPage';
import ManageQuizPage from './ManageQuizPage';

const Home = () => {
  const [userId, setUserId] = useState(null);
  const navigate = useNavigate(); // Khởi tạo useNavigate để điều hướng

  useEffect(() => {
    // Lấy userId từ localStorage
    const storedUserId = localStorage.getItem('userId');
    if (storedUserId) {
      setUserId(storedUserId);
    }
  }, []); // Chạy 1 lần khi component được render

  // Hàm đăng xuất
  const handleLogout = () => {
    localStorage.removeItem('userId');
    navigate('/login'); // Chuyển hướng về trang đăng nhập
  };

  return (
    <div className="home-container">
      <header className="home-header">
        <h1>Quiz Application</h1>
      </header>

      <div>
        {userId ? (
          <div>
            <p>User ID: {userId}</p>
            <button onClick={handleLogout}>Đăng xuất</button> {/* Nút đăng xuất */}
          </div>
        ) : (
          <p>Please log in to see your User ID</p>  // Hiển thị nếu chưa đăng nhập
        )}
      </div>

      <div className="home-content">
        <aside className="home-menu">
          <nav>
            <ul>
              <li>
                <NavLink to="/home" end className={({ isActive }) => isActive ? 'active' : ''}>
                  Trang chủ
                </NavLink>
              </li>
              <li>
                <NavLink to="/home/create-quiz" className={({ isActive }) => isActive ? 'active' : ''}>
                  Tạo Quiz
                </NavLink>
              </li>
              <li>
                <NavLink to="/home/manage-quiz" className={({ isActive }) => isActive ? 'active' : ''}>
                  Quản lý Quiz
                </NavLink>
              </li>
            </ul>
          </nav>
        </aside>

        <main className="home-main">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="create-quiz" element={<CreateQuizPage />} />
            <Route path="manage-quiz" element={<ManageQuizPage />} />
          </Routes>
        </main>
      </div>

      <footer className="home-footer">
        <p>&copy; 2024 Quiz Application. All rights reserved.</p>
      </footer>
    </div>
  );
};

export default Home;
