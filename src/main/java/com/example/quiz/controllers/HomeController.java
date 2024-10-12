package com.example.quiz.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")  // Điều hướng khi người dùng truy cập vào trang chủ
    public String home(Model model) {
        model.addAttribute("message", "Chào mừng đến với Quiz App!");
        return "index";  // Trả về template có tên "index.html"
    }
}
