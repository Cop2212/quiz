import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './ManageQuizPage.css'; // Import CSS

function ManageQuizPage() {
  const [quizzes, setQuizzes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [editQuiz, setEditQuiz] = useState(null); // State để lưu quiz đang sửa
  const [modalOpen, setModalOpen] = useState(false); // State để quản lý mở/đóng modal
  const [modifiedQuizzes, setModifiedQuizzes] = useState([]); // State để lưu quiz có thay đổi trạng thái

  const userId = localStorage.getItem('userId'); // Lấy userId từ localStorage

  useEffect(() => {
    if (!userId) {
      setError('Bạn cần đăng nhập để truy cập trang này.');
      setLoading(false);
      return;
    }

    // Gọi API để lấy danh sách quiz của người dùng
    axios.get(`http://localhost:8080/api/quiz/user/${userId}`)
      .then((response) => {
        console.log('Dữ liệu trả về:', response.data);
        setQuizzes(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error('Lỗi khi tải danh sách quiz:', error);
        setError('Đã xảy ra lỗi khi tải danh sách quiz.');
        setLoading(false);
      });
  }, [userId]);

  const handleDelete = (quizId) => {
    console.log(quizzes);  // Kiểm tra dữ liệu trả về

    if (!quizId) {
      console.error('Quiz ID không hợp lệ');
      setError('Quiz ID không hợp lệ.');
      return;
    }

    axios.delete(`http://localhost:8080/api/quiz/delete/${quizId}`)
      .then((response) => {
        console.log('Xóa quiz thành công:', response);
        setQuizzes(quizzes.filter(quiz => quiz.quizId !== quizId)); // Dùng quizId thay vì id nếu đó là tên trường
      })
      .catch((error) => {
        console.error('Lỗi khi xóa quiz:', error);
        setError('Đã xảy ra lỗi khi xóa quiz. Vui lòng thử lại.');
      });
  };

  const handleEdit = (quizId) => {
    // Tìm quiz cần sửa
    const quizToEdit = quizzes.find(quiz => quiz.quizId === quizId);
    setEditQuiz(quizToEdit);  // Lưu quiz đang sửa vào state
    setModalOpen(true);  // Mở modal
  };

  const handleSaveEdit = () => {
    if (!editQuiz) {
      return;
    }

    axios.put(`http://localhost:8080/api/quiz/edit/${editQuiz.quizId}`, editQuiz)
      .then((response) => {
        console.log('Sửa quiz thành công:', response);
        // Cập nhật lại danh sách quiz sau khi sửa thành công
        setQuizzes(quizzes.map(quiz => quiz.quizId === editQuiz.quizId ? editQuiz : quiz));
        setModalOpen(false); // Đóng modal
      })
      .catch((error) => {
        console.error('Lỗi khi sửa quiz:', error);
        setError('Đã xảy ra lỗi khi sửa quiz. Vui lòng thử lại.');
      });
  };

  const handleModalClose = () => {
    setModalOpen(false); // Đóng modal
  };

  // Cập nhật trạng thái public/private của quiz
  const handlePrivacyChange = (quizId, isPublic) => {
    const updatedQuiz = quizzes.map((quiz) =>
      quiz.quizId === quizId ? { ...quiz, isPrivate: !isPublic } : quiz
    );
    setQuizzes(updatedQuiz);

    // Lưu các quiz đã thay đổi vào modifiedQuizzes
    const modified = updatedQuiz.filter((quiz) => quiz.quizId === quizId && quiz.isPrivate !== isPublic);
    if (modified.length > 0) {
      setModifiedQuizzes([...modifiedQuizzes, modified[0]]);
    }
  };

  const handleSavePrivacyChanges = () => {
    // Gửi tất cả các quiz đã thay đổi trạng thái đến server
    modifiedQuizzes.forEach((quiz) => {
      axios.put(`http://localhost:8080/api/quiz/updatePrivacy/${quiz.quizId}`, null, {
        params: { isPublic: !quiz.isPrivate },  // Gửi trạng thái public (false nếu là private)
      })
        .then((response) => {
          console.log('Cập nhật trạng thái quiz thành công:', response);
        })
        .catch((error) => {
          console.error('Lỗi khi cập nhật trạng thái quiz:', error);
          setError('Đã xảy ra lỗi khi cập nhật trạng thái quiz.');
        });
    });
    setModifiedQuizzes([]); // Xóa danh sách modifiedQuizzes sau khi lưu
  };

  if (loading) return <p style={{ color: 'blue' }}>Đang tải...</p>;
  if (error) return <p style={{ color: 'red' }}>{error}</p>;

  return (
    <div>
      <h2>Danh sách Quiz của bạn</h2>
      {quizzes.length > 0 ? (
        <ul>
          {quizzes.map((quiz) => (
            <li key={quiz.quizId}>
              {quiz.quizName} - Mã: {quiz.codeQuiz} - Thời gian: {quiz.timeLimit} phút
              <button onClick={() => handleEdit(quiz.quizId)}>Sửa</button>
              <button onClick={() => handleDelete(quiz.quizId)}>Xóa</button>
              {/* Thêm checkbox để thay đổi trạng thái public */}
              <label>
                Private:
                <input
                  type="checkbox"
                  checked={quiz.isPrivate} // Giá trị thực tế từ quiz
                  onChange={(e) => handlePrivacyChange(quiz.quizId, !e.target.checked)} // Đảo ngược trạng thái
                />
              </label>
            </li>
          ))}
        </ul>
      ) : (
        <p>Bạn chưa tạo quiz nào.</p>
      )}

      {/* Nút lưu tất cả thay đổi trạng thái */}
      {modifiedQuizzes.length > 0 && (
        <button onClick={handleSavePrivacyChanges}>Lưu thay đổi trạng thái</button>
      )}

      {/* Modal sửa quiz */}
      {modalOpen && (
        <div className="modal">
          <div className="modal-content">
            <h3>Sửa Quiz</h3>
            <label>Tên quiz:</label>
            <input
              type="text"
              value={editQuiz?.quizName || ''}
              onChange={(e) => setEditQuiz({ ...editQuiz, quizName: e.target.value })}
            />
            <br />
            <label>Mã quiz:</label>
            <input
              type="text"
              value={editQuiz?.codeQuiz || ''}
              onChange={(e) => setEditQuiz({ ...editQuiz, codeQuiz: e.target.value })}
            />
            <br />
            <label>Thời gian:</label>
            <input
              type="number"
              value={editQuiz?.timeLimit || ''}
              onChange={(e) => setEditQuiz({ ...editQuiz, timeLimit: e.target.value })}
            />
            <br />
            <button onClick={handleSaveEdit}>Lưu</button>
            <button onClick={handleModalClose}>Đóng</button>
          </div>
        </div>
      )}
    </div>
  );
}

export default ManageQuizPage;
