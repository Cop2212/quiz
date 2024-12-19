package com.example.quiz.repository;

import com.example.quiz.model.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    // Thêm phương thức tìm kết quả quiz theo quizId và userId
    Optional<QuizResult> findByQuizIdAndUserId(Long quizId, Long userId);
    boolean existsByUserIdAndQuizId(Long userId, Long quizId);
}
