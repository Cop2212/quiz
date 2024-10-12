package com.example.quiz.services;

import com.example.quiz.entities.User;
import com.example.quiz.repositories.UserRepository;
import com.example.quiz.security.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Đăng ký người dùng mới
    public void registerUser(String username, String password, String email, String role) {
        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
        PasswordEncoder encoder = PasswordEncoder.getInstance();
        String encodedPassword = encoder.encodePassword(password);

        // Tạo đối tượng User và lưu vào database
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setEmail(email);
        user.setRole(role);

        userRepository.save(user);
    }

    // Đăng nhập người dùng
    public boolean loginUser(String username, String rawPassword) {
        // Tìm kiếm người dùng theo username
        User user = userRepository.findByUsername(username);
        if (user != null) {
            // Kiểm tra mật khẩu đã mã hóa với mật khẩu người dùng nhập
            PasswordEncoder encoder = PasswordEncoder.getInstance();
            return encoder.matches(rawPassword, user.getPassword());
        }
        return false;
    }
}
