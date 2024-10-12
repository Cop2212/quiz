package com.example.quiz.repositories;

import com.example.quiz.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);  // Tìm người dùng dựa trên tên đăng nhập

    User findByEmail(String email); // Tìm người dùng dựa trên email

    User findByResetToken(String resetToken); // Tìm người dùng dựa trên reset token

    // Thêm phương thức kiểm tra tồn tại username
    boolean existsByUsername(String username);

    // Thêm phương thức kiểm tra tồn tại email
    boolean existsByEmail(String email);
}
