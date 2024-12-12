package com.example.quiz.controller;

import com.example.quiz.model.User;
import com.example.quiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tài khoản đã tồn tại");
        }

        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email đã tồn tại");
        }

        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tạo tài khoản thành công");
    }

    @GetMapping("/check-email/{email}")
    public boolean checkEmail(@PathVariable String email) {
        return userService.existsByEmail(email);
    }

    @GetMapping("/check-username/{username}")
    public boolean checkUsername(@PathVariable String username) {
        return userService.existsByUsername(username);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body("Thiếu thông tin đăng nhập");
        }

        // Tìm người dùng chỉ theo username
        User user = userService.findByUsername(loginRequest.getUsername());

        if (user == null || !userService.checkPassword(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai tài khoản hoặc mật khẩu");
        }

        // Nếu đăng nhập thành công, trả về thông tin người dùng (bao gồm id)
        return ResponseEntity.ok(new LoginResponse(user.getId(), user.getUsername()));
    }

    // DTO cho phản hồi đăng nhập
    public static class LoginResponse {
        private Long id;
        private String username;

        public LoginResponse(Long id, String username) {
            this.id = id;
            this.username = username;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

}
