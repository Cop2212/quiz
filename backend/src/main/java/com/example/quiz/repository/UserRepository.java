package com.example.quiz.repository;

import com.example.quiz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Tìm người dùng theo username
    Optional<User> findByUsername(String username);

    // Kiểm tra xem username có tồn tại không
    boolean existsByUsername(String username);

    // Kiểm tra xem email có tồn tại không
    boolean existsByEmail(String email);
}
