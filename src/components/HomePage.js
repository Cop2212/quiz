import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom'; // Thêm useNavigate để điều hướng
import './HomePage.css';

function HomePage() {
  const [quizCode, setQuizCode] = useState(''); // State để lưu mã quiz nhập vào
  const [quizzes, setQuizzes] = useState([]); // State để lưu danh sách quiz
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate(); // Khai báo navigate để điều hướng

  // Hàm gọi API để lấy quiz theo mã
  const handleSearch = () => {
    if (!quizCode) {
      setError('Vui lòng nhập mã quiz.');
      return;
    }

    setLoading(true);
    setError(null);

    // Lấy userId từ localStorage
    const userId = localStorage.getItem('userId');

    // Gọi API để lấy danh sách quiz có mã quizCode
    axios.get(`http://localhost:8080/api/quiz/code/${quizCode}`, {
      params: { userId },
    })
      .then((response) => {
        setQuizzes(response.data); // Lưu dữ liệu quiz trả về
        setLoading(false);
      })
      .catch((error) => {
        console.error('Lỗi khi tìm quiz:', error);
        setError('Không tìm thấy quiz nào với mã này.');
        setLoading(false);
      });
  };

  // Hàm xử lý khi nhấn nút "Làm bài"
  const handleTakeQuiz = (quizId) => {
    // Điều hướng đến trang làm bài của quiz đó
    navigate(`/take-quiz/${quizId}`);
  };

  return (
    <div>
      <h2>Trang chủ</h2>
      <p>Đây là nội dung của trang chủ.</p>

      {/* Trường nhập liệu mã quiz */}
      <input
        type="text"
        placeholder="Nhập mã quiz"
        value={quizCode}
        onChange={(e) => setQuizCode(e.target.value)} // Cập nhật giá trị mã quiz
      />
      <button onClick={handleSearch}>Tìm Quiz</button>

      {loading && <p>Đang tải...</p>} {/* Hiển thị thông báo khi đang tải */}
      {error && <p style={{ color: 'red' }}>{error}</p>} {/* Hiển thị lỗi nếu có */}

      {/* Hiển thị danh sách quiz */}
      {quizzes.length > 0 && (
        <ul>
          {quizzes.map((quiz) => (
            <li key={quiz.quizId}>
              {quiz.quizName} - Mã: {quiz.codeQuiz} - Thời gian: {quiz.timeLimit} phút
              <span>{quiz.isPrivate ? ' (Private)' : ' (Public)'}</span>
              {/* Thêm nút "Làm bài" */}
              <button onClick={() => handleTakeQuiz(quiz.quizId)}>Làm bài</button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default HomePage;
