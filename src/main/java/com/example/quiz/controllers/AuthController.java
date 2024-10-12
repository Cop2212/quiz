package com.example.quiz.controllers;

import com.example.quiz.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Trang chính
    @GetMapping("/")
    public String index() {
        return "index"; // Trả về file index.html
    }

    // Hiển thị trang quên mật khẩu
    @GetMapping("/auth/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot_password"; // Trả về trang forgot_password.html
    }

    // Xử lý yêu cầu quên mật khẩu
    @PostMapping("/auth/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        try {
            userService.generateResetToken(email);
            return "redirect:/auth/forgot-password?success"; // Chuyển hướng sau khi gửi yêu cầu thành công
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "forgot_password"; // Quay lại trang quên mật khẩu với thông báo lỗi
        }
    }

    // Hiển thị trang đặt lại mật khẩu với token
    @GetMapping("/auth/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset_password"; // Trả về trang reset_password.html
    }

    // Xử lý đặt lại mật khẩu
    @PostMapping("/auth/reset-password")
    public String processResetPassword(@RequestParam("token") String token, @RequestParam("password") String newPassword, Model model) {
        try {
            userService.updatePassword(token, newPassword);
            return "redirect:/auth/reset-password?success"; // Chuyển hướng sau khi đặt lại mật khẩu thành công
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("token", token); // Đảm bảo token vẫn được gửi lại để người dùng không phải nhập lại
            return "reset_password"; // Quay lại trang đặt lại mật khẩu với thông báo lỗi
        }
    }
}
