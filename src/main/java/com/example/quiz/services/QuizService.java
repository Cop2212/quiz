package com.example.quiz.services;

import com.example.quiz.entities.Quiz;
import com.example.quiz.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    // Lấy danh sách tất cả các quiz
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    // Lấy quiz theo ID
    public Quiz getQuizById(Long id) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(id);
        if (optionalQuiz.isPresent()) {
            return optionalQuiz.get();
        } else {
            throw new RuntimeException("Quiz không tồn tại với id: " + id);
        }
    }
}
//bom vua thay doicode