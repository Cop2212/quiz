import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import axios from 'axios';
import './LoginPage.css'; // Import CSS

const Login = () => {
  const [formData, setFormData] = useState({
    username: '', // Thay đổi từ usernameOrEmail thành username
    password: '',
  });

  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/api/users/login', formData);
      console.log('Response:', response.data);  // Kiểm tra phản hồi API

      // Kiểm tra xem response có chứa id không
      if (response.data && response.data.id) {
        const userId = response.data.id;  // Lấy userId từ phản hồi API
        localStorage.setItem('userId', userId);  // Lưu vào localStorage
        navigate('/home');  // Chuyển hướng tới trang home
      } else {
        setError('Lỗi khi lấy thông tin người dùng');
      }
    } catch (error) {
      console.error('There was an error logging in:', error);
      if (error.response && error.response.status === 401) {
        setError('Sai tài khoản hoặc mật khẩu');
      } else {
        setError('Đã xảy ra lỗi, vui lòng thử lại');
      }
    }
  };


  return (
    <div className="login-form">
      <h2>Đăng nhập</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Username</label>
          <input
            type="text"
            name="username"
            value={formData.username}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Password</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>
        <button type="submit">Đăng nhập</button>
      </form>
      {error && <p>{error}</p>}
    </div>
  );
};

export default Login;
