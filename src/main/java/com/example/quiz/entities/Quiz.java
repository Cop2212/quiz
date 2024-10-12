package com.example.quiz.entities;

import jakarta.persistence.*;


@Entity
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    // getters and setters
}
