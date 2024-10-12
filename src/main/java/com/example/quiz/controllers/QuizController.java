package com.example.quiz.controllers;

import com.example.quiz.entities.Quiz;
import com.example.quiz.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping("/quiz/{id}")
    public String showQuizDetails(@PathVariable("id") Long id, Model model) {
        Quiz quiz = quizService.getQuizById(id);
        model.addAttribute("quiz", quiz);
        return "quiz_details";  // Trả về trang chi tiết quiz
    }
}
