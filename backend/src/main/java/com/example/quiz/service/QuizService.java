package com.example.quiz.service;

import com.example.quiz.model.Quiz;
import com.example.quiz.repository.QuizRepository;
import com.example.quiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private final QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz updateQuiz(Long quizId, Quiz updatedQuiz) {
        // Tìm quiz theo ID
        Quiz existingQuiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz không tồn tại"));

        // Cập nhật các trường cần thiết
        existingQuiz.setQuizName(updatedQuiz.getQuizName());
        existingQuiz.setCodeQuiz(updatedQuiz.getCodeQuiz());
        existingQuiz.setTimeLimit(updatedQuiz.getTimeLimit());

        // Lưu lại quiz đã cập nhật
        return quizRepository.save(existingQuiz);
    }

    public Quiz updatePrivacy(Long quizId, boolean isPublic) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz không tồn tại"));
        quiz.setPrivate(!isPublic);  // Nếu isPublic = true, set isPrivate = false (công khai)
        return quizRepository.save(quiz);
    }

    public List<Quiz> findByCodeQuiz(String codeQuiz) {
        return quizRepository.findByCodeQuiz(codeQuiz);
    }

    // Tìm quiz theo ID
    public Optional<Quiz> findById(Long quizId) {
        return quizRepository.findById(quizId);
    }

    public Quiz getQuizById(Long quizId) {
        // Tìm quiz theo quizId
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz không tồn tại"));

        // Trả về quiz (bao gồm các câu hỏi đã liên kết)
        return quiz;
    }
}
