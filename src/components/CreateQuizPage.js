import React, { useState } from 'react';
import './CreateQuizPage.css';

function CreateQuizPage() {
  const [quizName, setQuizName] = useState('');
  const [codeQuiz, setCodeQuiz] = useState('');
  const [timeLimit, setTimeLimit] = useState(0);
  const [questions, setQuestions] = useState([]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const userId = localStorage.getItem('userId');  // Lấy userId từ localStorage
    if (!userId) {
      alert('Vui lòng đăng nhập để tạo quiz.');
      return;
    }

    if (timeLimit <= 0) {
      alert('Thời gian làm bài phải lớn hơn 0!');
      return;
    }

    const newQuiz = { quizName, codeQuiz, timeLimit, questions, userId }; // Thêm userId vào payload

    try {
      const response = await fetch('http://localhost:8080/api/quiz/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newQuiz),
      });

      if (response.ok) {
        alert('Quiz đã được tạo thành công!');
      } else {
        alert('Có lỗi xảy ra khi tạo quiz.');
      }
    } catch (error) {
      alert('Có lỗi kết nối đến server.');
    }

    console.log('Payload gửi lên:', JSON.stringify(newQuiz, null, 2));
  };


  const handleAddCustomQuestion = () => {
    const newQuestion = { questionText: '', options: ['', '', '', ''], correctAnswerIndex: 0 };
    if (isDuplicateQuestion(newQuestion)) {
        alert('Câu hỏi này đã tồn tại!');
        return;
      }
    setQuestions([...questions, newQuestion]);
  };

  const handleCustomInputChange = (index, field, value) => {
    const newQuestions = [...questions];
    if (field === 'questionText') {
      newQuestions[index].questionText = value;
    } else if (field.startsWith('option')) {
      const optionIndex = parseInt(field.replace('option', ''), 10);
      newQuestions[index].options[optionIndex] = value;
    } else if (field === 'correctAnswerIndex') {
      newQuestions[index].correctAnswerIndex = parseInt(value, 10);
    }
    setQuestions(newQuestions);
  };

  const handleDeleteQuestion = (index) => {
    const newQuestions = [...questions];
    newQuestions.splice(index, 1);
    setQuestions(newQuestions);
  };

  const shuffleAnswers = (index) => {
    const newQuestions = [...questions];
    const question = newQuestions[index];

    const shuffledOptions = [...question.options];
    const shuffledIndexMap = Array.from({ length: shuffledOptions.length }, (_, i) => i);

    for (let i = shuffledIndexMap.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [shuffledIndexMap[i], shuffledIndexMap[j]] = [shuffledIndexMap[j], shuffledIndexMap[i]];
    }

    const newOptions = shuffledIndexMap.map((i) => shuffledOptions[i]);
    const newCorrectAnswerIndex = shuffledIndexMap.indexOf(question.correctAnswerIndex);

    newQuestions[index] = {
      ...question,
      options: newOptions,
      correctAnswerIndex: newCorrectAnswerIndex,
    };

    setQuestions(newQuestions);
  };

  const handleFileUpload = (e) => {
    const file = e.target.files[0];
    if (!file) return;

    if (file.type !== 'text/plain') {
      alert('Định dạng file không hợp lệ. Vui lòng chọn file .txt');
      return;
    }

    const reader = new FileReader();
    reader.onload = (event) => {
      try {
        const lines = event.target.result.split('\n').map(line => line.trim());
        const parsedQuestions = [];
        let currentQuestion = null;
        let options = [];

        lines.forEach((line) => {
          if (line.startsWith('Câu hỏi:')) {
            if (currentQuestion && options.length === 4) {
              const newQuestion = {
                questionText: currentQuestion,
                options,
                correctAnswerIndex: 0,
              };
              if (!isDuplicateQuestion(newQuestion)) {
                parsedQuestions.push(newQuestion);
              }
            }
            currentQuestion = line.replace('Câu hỏi:', '').trim();
            options = [];
          } else if (line.startsWith('Đáp án:')) {
            options.push(line.replace('Đáp án:', '').trim());
          }
        });

        if (currentQuestion && options.length === 4) {
          const newQuestion = {
            questionText: currentQuestion,
            options,
            correctAnswerIndex: 0,
          };
          if (!isDuplicateQuestion(newQuestion)) {
            parsedQuestions.push(newQuestion);
          }
        }

        if (parsedQuestions.length === 0) {
          throw new Error('File không chứa dữ liệu hợp lệ.');
        }

        setQuestions((prev) => [...prev, ...parsedQuestions]);
        alert('File đã được đọc và câu hỏi đã được thêm vào!');
      } catch (error) {
        alert(`Không thể đọc file: ${error.message}`);
      }
    };

    reader.readAsText(file);
  };


  const shuffleQuestions = () => {
    const shuffledQuestions = [...questions];
    for (let i = shuffledQuestions.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [shuffledQuestions[i], shuffledQuestions[j]] = [shuffledQuestions[j], shuffledQuestions[i]];
    }
    setQuestions(shuffledQuestions);
  };

const isDuplicateQuestion = (newQuestion) => {
  return questions.some((q) => q.questionText === newQuestion.questionText);
};




  return (
      <div className="container">
        <h2>Tạo Quiz</h2>

        <form onSubmit={handleSubmit}>
          <div>
            <label>Tên Quiz:</label>
            <input
              type="text"
              value={quizName}
              onChange={(e) => setQuizName(e.target.value)}
              required
            />
          </div>

          <div>
            <label>Mã Quiz:</label>
            <input
              type="text"
              value={codeQuiz}
              onChange={(e) => setCodeQuiz(e.target.value)}
              required
            />
          </div>

          <div>
            <label>Thời gian làm bài (phút):</label>
            <input
              type="number"
              value={timeLimit}
              onChange={(e) => setTimeLimit(parseInt(e.target.value, 10) || 0)}
              min="1"
              required
            />
          </div>

          <div>
            <h3>Câu hỏi tự nhập:</h3>
            {questions.map((question, index) => (
              <div key={index} className="question-container">
                <h4>{`Câu hỏi ${index + 1}:`}</h4>
                <label>
                  Câu hỏi:
                  <input
                    type="text"
                    value={question.questionText}
                    onChange={(e) => handleCustomInputChange(index, 'questionText', e.target.value)}
                    placeholder="Nhập câu hỏi"
                    required
                  />
                </label>
                <div>
                  {question.options.map((option, optionIndex) => (
                    <div key={optionIndex} className="option-container">
                      <label>
                        Đáp án {optionIndex + 1}:
                        <input
                          type="text"
                          value={option}
                          onChange={(e) => handleCustomInputChange(index, `option${optionIndex}`, e.target.value)}
                          placeholder={`Nhập đáp án ${optionIndex + 1}`}
                          required
                        />
                      </label>
                    </div>
                  ))}
                </div>
                <div>
                  <label>Đáp án đúng:</label>
                  <select
                    value={question.correctAnswerIndex}
                    onChange={(e) => handleCustomInputChange(index, 'correctAnswerIndex', e.target.value)}
                  >
                    {question.options.map((_, optionIndex) => (
                      <option key={optionIndex} value={optionIndex}>
                        Đáp án {optionIndex + 1}
                      </option>
                    ))}
                  </select>
                </div>
                <button type="button" onClick={() => shuffleAnswers(index)}>Trộn đáp án</button>
                <button type="button" onClick={() => handleDeleteQuestion(index)}>Xóa câu hỏi</button>
              </div>
            ))}

            <button type="button" onClick={handleAddCustomQuestion}>Thêm câu hỏi mới</button>
          </div>

          <div className="upload-container">
            <h3>Chọn file để thêm câu hỏi:</h3>
            <input type="file" accept=".txt" onChange={handleFileUpload} />
          </div>

          <div>
            <button type="button" onClick={shuffleQuestions}>
              Trộn câu hỏi
            </button>
          </div>

          <button type="submit">Tạo Quiz</button>
        </form>
      </div>
    );
}

export default CreateQuizPage;
