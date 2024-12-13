import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import axios from 'axios';
import './SignupPage.css'; // Import CSS

const Signup = () => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
  });

  const [errors, setErrors] = useState({
    username: '',
    email: '',
    password: '',
  });

  const navigate = useNavigate(); // Hook để chuyển hướng

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
    // Xóa lỗi tương ứng khi người dùng thay đổi giá trị trong form
    setErrors({
      ...errors,
      [name]: '',
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Kiểm tra mật khẩu và xác nhận mật khẩu
    if (formData.password !== formData.confirmPassword) {
      setErrors({ ...errors, password: 'Mật khẩu và xác nhận mật khẩu không khớp' });
      return;
    }

    try {
      const checkEmailResponse = await axios.get(`http://localhost:8080/api/users/check-email/${formData.email}`);
      const checkUsernameResponse = await axios.get(`http://localhost:8080/api/users/check-username/${formData.username}`);

      if (checkEmailResponse.data) {
        setErrors({ ...errors, email: 'Email đã tồn tại' });
        return;
      }

      if (checkUsernameResponse.data) {
        setErrors({ ...errors, username: 'Username đã tồn tại' });
        return;
      }

      const response = await axios.post('http://localhost:8080/api/users/signup', formData);
      console.log('User registered successfully:', response.data);

      // Hiển thị thông báo thành công
      alert('Đăng ký thành công! Bạn có thể đăng nhập ngay.');

      // Chuyển hướng về trang login
      navigate('/login');
    } catch (error) {
      console.error('Có lỗi xảy ra khi đăng ký người dùng:', error);
      alert('Đã xảy ra lỗi khi đăng ký, vui lòng thử lại!');
    }
  };

  return (
    <div className="signup-form">
      <h2>Đăng ký tài khoản</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Username</label>
          <input
            type="text"
            name="username"
            value={formData.username}
            onChange={handleChange}
            className={errors.username ? 'error' : ''}
            required
          />
          {errors.username && <p className="error-message">{errors.username}</p>}
        </div>
        <div>
          <label>Email</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            className={errors.email ? 'error' : ''}
            required
          />
          {errors.email && <p className="error-message">{errors.email}</p>}
        </div>
        <div>
          <label>Password</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            className={errors.password ? 'error' : ''}
            required
          />
        </div>
        <div>
          <label>Confirm Password</label>
          <input
            type="password"
            name="confirmPassword"
            value={formData.confirmPassword}
            onChange={handleChange}
            className={errors.password ? 'error' : ''}
            required
          />
          {errors.password && <p className="error-message">{errors.password}</p>}
        </div>
        <button type="submit">Đăng ký</button>
      </form>
    </div>
  );
};

export default Signup;
