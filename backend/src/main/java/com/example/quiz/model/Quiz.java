package com.example.quiz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String quizName;
    private String codeQuiz;
    private int timeLimit;

    @Column(name = "is_private")
    private boolean isPrivate = true;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Liên kết quiz với user

    @JsonManagedReference
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPublic(boolean isPublic) {
        this.isPrivate = isPublic;  // Nếu public thì đổi trạng thái thành false
    }
}
