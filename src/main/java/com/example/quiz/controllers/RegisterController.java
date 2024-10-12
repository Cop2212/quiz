package com.example.quiz.controllers;

import com.example.quiz.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    // Hiển thị trang đăng ký
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    // Xử lý yêu cầu đăng ký
    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("email") String email,
                               Model model) {  // Thêm Model để truyền dữ liệu về giao diện
        try {
            // Đăng ký với vai trò mặc định là "USER"
            userService.registerUser(username, password, email, "USER");
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu thành công
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage()); // Thêm thông báo lỗi vào mô hình
            return "register"; // Quay lại trang đăng ký với thông báo lỗi
        }
    }
}
