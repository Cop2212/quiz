package com.example.quiz.controllers;

import com.example.quiz.entities.Quiz;
import com.example.quiz.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private QuizService quizService;

    @GetMapping("/home")
    public String showHomePage(Model model) {
        // Lấy danh sách tất cả các quiz
        List<Quiz> quizzes = quizService.getAllQuizzes();
        model.addAttribute("quizzes", quizzes);

        return "home"; // Trả về view "home.html"
    }
}
