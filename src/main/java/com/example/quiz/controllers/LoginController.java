package com.example.quiz.controllers;

import com.example.quiz.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            RedirectAttributes redirectAttributes) {
        if (userService.loginUser(username, password)) {
            return "redirect:/home"; // Chuyển hướng đến trang chủ sau khi đăng nhập thành công
        } else {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
            return "redirect:/login"; // Hiển thị lại form đăng nhập nếu thất bại
        }
    }
}
