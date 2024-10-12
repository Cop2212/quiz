package com.example.quiz.services;

import com.example.quiz.entities.User;
import com.example.quiz.repositories.UserRepository;
import com.example.quiz.security.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // Đăng ký người dùng mới
    public void registerUser(String username, String password, String email, String role) {
        // Kiểm tra nếu username hoặc email đã tồn tại
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã tồn tại!");
        }

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

    // Sinh reset token và gửi email đặt lại mật khẩu
    public void generateResetToken(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Email không tồn tại");
        }

        // Sinh reset token
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepository.save(user);

        // Gửi email cho người dùng
        String resetLink = "http://localhost:8080/auth/reset-password?token=" + token;
        try {
            sendResetEmail(user.getEmail(), resetLink);
        } catch (Exception e) {
            throw new RuntimeException("Không thể gửi email. Vui lòng thử lại sau.");
        }
    }

    // Tìm người dùng theo reset token và cập nhật mật khẩu
    public void updatePassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token);
        if (user == null) {
            throw new RuntimeException("Token không hợp lệ");
        }

        // Mã hóa mật khẩu mới và cập nhật người dùng
        PasswordEncoder encoder = PasswordEncoder.getInstance();
        user.setPassword(encoder.encodePassword(newPassword));
        user.setResetToken(null); // Xóa token sau khi sử dụng
        userRepository.save(user);
    }

    // Gửi email đặt lại mật khẩu
    private void sendResetEmail(String email, String resetLink) {
        String subject = "Yêu cầu đặt lại mật khẩu";
        String body = "Nhấp vào liên kết sau để đặt lại mật khẩu của bạn: \n" + resetLink;

        try {
            emailService.sendEmail(email, subject, body);
        } catch (Exception e) {
            e.printStackTrace(); // Ghi lại lỗi để kiểm tra
            throw new RuntimeException("Có lỗi xảy ra khi gửi email: " + e.getMessage());
        }
    }
}
