package com.example.quiz.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    private String answerText;
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    // getters and setters
}

