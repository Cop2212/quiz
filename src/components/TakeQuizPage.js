import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import './TakeQuizPage.css'; // Import CSS

function TakeQuizPage() {
  const { quizId } = useParams(); // Lấy quizId từ URL
  const [quiz, setQuiz] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [answers, setAnswers] = useState([]); // Lưu câu trả lời người dùng chọn

  // Hàm gọi API để lấy thông tin quiz theo quizId
  useEffect(() => {
    setLoading(true);
    setError(null);

    // Lấy userId từ localStorage
    const userId = localStorage.getItem('userId');

    // Gọi API để lấy thông tin quiz theo quizId
    axios.get(`http://localhost:8080/api/quiz/${quizId}`, {
      params: { userId },
    })
      .then((response) => {
        setQuiz(response.data); // Lưu thông tin quiz vào state
        setLoading(false);
      })
      .catch((error) => {
        console.error('Lỗi khi lấy thông tin quiz:', error);
        setError('Không thể lấy thông tin quiz.');
        setLoading(false);
      });
  }, [quizId]);

  // Hàm để xử lý khi người dùng chọn một câu trả lời
  const handleAnswerChange = (questionIndex, selectedOption) => {
    setAnswers((prevAnswers) => {
      const updatedAnswers = [...prevAnswers];
      updatedAnswers[questionIndex] = selectedOption;
      return updatedAnswers;
    });
  };

  const handleSubmit = () => {
    const userId = localStorage.getItem('userId');
    // Gọi API để gửi câu trả lời
    axios.post(`http://localhost:8080/api/quiz/submit`, {
      quizId,
      userId,
      userAnswers: answers,
    })
      .then(response => {
        alert('Đã nộp bài!');
      })
      .catch(error => {
        console.error('Lỗi khi nộp bài:', error);
      });
  };

  if (loading) {
    return <p className="loading">Đang tải...</p>;
  }

  if (error) {
    return <p className="error-message">{error}</p>;
  }

  return (
    <div className="quiz-container">
      <h2>{quiz.quizName}</h2>
      <p>Thời gian: {quiz.timeLimit} phút</p>
      <p>{quiz.isPrivate ? 'Quiz này là riêng tư' : 'Quiz này công khai'}</p>

      <form>
        {quiz.questions.map((question, index) => (
          <div key={index} className="question">
            <p>{question.questionText}</p>
            <div className="options">
              {question.options.map((option, optionIndex) => (
                <div key={optionIndex}>
                  <input
                    type="radio"
                    id={`question${index}_option${optionIndex}`}
                    name={`question${index}`}
                    value={optionIndex}
                    checked={answers[index] === optionIndex}
                    onChange={() => handleAnswerChange(index, optionIndex)}
                  />
                  <label htmlFor={`question${index}_option${optionIndex}`}>{option}</label>
                </div>
              ))}
            </div>
          </div>
        ))}
        <button type="button" onClick={handleSubmit}>Nộp bài</button>
      </form>
    </div>
  );
}

export default TakeQuizPage;
