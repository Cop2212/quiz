package com.example.quiz.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    private String questionText;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    // getters and setters
}

